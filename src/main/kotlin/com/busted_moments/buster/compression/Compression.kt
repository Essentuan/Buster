package com.busted_moments.buster.compression

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import java.lang.Thread.currentThread
import java.util.IdentityHashMap
import java.util.zip.Deflater
import java.util.zip.Inflater

object Compression {
    @OptIn(DelicateCoroutinesApi::class)
    private val pool = newFixedThreadPoolContext(5, "compression")
    private val engines: MutableMap<Thread, Pair<Inflater, Deflater>> = IdentityHashMap()

    private val Thread.engine: Pair<Inflater, Deflater>
        get() {
            var engine = engines[this]

            return if (engine != null)
                engine
            else
                synchronized(engines) {
                    engines.computeIfAbsent(this) {
                        Inflater(true) to Deflater(Deflater.BEST_COMPRESSION, true)
                    }
                }
        }

    suspend fun deflate(bytes: ByteArray): ByteArray {
        return withContext(pool) {
            val (_, deflater) = currentThread().engine

            deflater.deflateFully(bytes).also {
                deflater.reset()
            }
        }
    }

    suspend fun inflate(bytes: ByteArray): ByteArray {
        return withContext(pool) {
            val (inflater, _) = currentThread().engine

            inflater.inflateFully(bytes).also {
                inflater.reset()
            }
        }
    }
}