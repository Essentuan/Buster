package com.busted_moments.buster.protocol

import com.busted_moments.buster.Buster
import com.busted_moments.buster.exceptions.PacketFormatException
import net.essentuan.esl.Result
import net.essentuan.esl.json.Json
import net.essentuan.esl.json.json
import net.essentuan.esl.model.Model.Companion.wrap
import net.essentuan.esl.reflections.extensions.isAbstract
import net.essentuan.esl.unsafe

interface Packet : Buster.Type {
    override fun save(data: Json): Json = json(data) {
        "type" to this@Packet.javaClass.name
    }

    companion object {
        operator fun invoke(json: Json): Result<out Packet> {
            if (!json.isString("type"))
                return Result.fail(PacketFormatException(message = "Missing packet type!"))

            val name = json.getString("type")!!

            val cls: Class<*>? =
                if (name.startsWith("com.busted_moments.buster.protocol"))
                    Class.forName(name)
                else
                    null

            if (cls == null || !Packet::class.java.isAssignableFrom(cls) || cls.isAbstract())
                return Result.fail(PacketFormatException(message = "Invalid packet type!"))

            return unsafe {
                @Suppress("UNCHECKED_CAST")
                json.wrap(cls as Class<out Packet>)
            }
        }
    }
}