package com.busted_moments.buster.compression

import io.ktor.util.cio.KtorDefaultPool
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.pool.useInstance
import java.nio.ByteBuffer
import java.util.zip.Deflater
import java.util.zip.Inflater

private val PADDED_EMPTY_CHUNK: ByteArray = byteArrayOf(0, 0, 0, 0xff.toByte(), 0xff.toByte())
private val EMPTY_CHUNK: ByteArray = byteArrayOf(0, 0, 0xff.toByte(), 0xff.toByte())

internal fun Deflater.deflateFully(data: ByteArray): ByteArray {
    setInput(data)

    val deflatedBytes = buildPacket {
        KtorDefaultPool.useInstance { buffer ->
            while (!needsInput()) {
                deflateTo(this@deflateFully, buffer, false)
            }

            while (deflateTo(this@deflateFully, buffer, true) != 0) {}
        }
    }

    if (deflatedBytes.endsWith(PADDED_EMPTY_CHUNK)) {
        return deflatedBytes.readBytes(deflatedBytes.remaining.toInt() - EMPTY_CHUNK.size).also {
            deflatedBytes.release()
        }
    }

    return buildPacket {
        writePacket(deflatedBytes)
        writeByte(0)
    }.readBytes()
}

internal fun Inflater.inflateFully(data: ByteArray): ByteArray {
    val dataToInflate = data + EMPTY_CHUNK
    setInput(dataToInflate)

    val packet = buildPacket {
        KtorDefaultPool.useInstance { buffer ->
            val limit = dataToInflate.size + bytesRead
            while (bytesRead < limit) {
                buffer.clear()
                val inflated = inflate(buffer.array(), buffer.position(), buffer.limit())
                buffer.position(buffer.position() + inflated)
                buffer.flip()

                writeFully(buffer)
            }
        }
    }

    return packet.readBytes()
}

private fun BytePacketBuilder.deflateTo(
    deflater: Deflater,
    buffer: ByteBuffer,
    flush: Boolean
): Int {
    buffer.clear()

    val deflated = if (flush) {
        deflater.deflate(buffer.array(), buffer.position(), buffer.limit(), Deflater.SYNC_FLUSH)
    } else {
        deflater.deflate(buffer.array(), buffer.position(), buffer.limit())
    }

    if (deflated == 0) {
        return 0
    }

    buffer.position(buffer.position() + deflated)
    buffer.flip()
    writeFully(buffer)

    return deflated
}

private fun ByteReadPacket.endsWith(data: ByteArray): Boolean {
    copy().apply {
        discard(remaining - data.size)
        return readBytes().contentEquals(data)
    }
}
