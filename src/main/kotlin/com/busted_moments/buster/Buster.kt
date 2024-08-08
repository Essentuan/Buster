package com.busted_moments.buster

import com.busted_moments.buster.api.Profile
import io.ktor.util.AttributeKey
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketExtension
import io.ktor.websocket.WebSocketExtensionFactory
import io.ktor.websocket.WebSocketExtensionHeader
import net.essentuan.esl.json.Json
import net.essentuan.esl.reflections.Reflections
import net.essentuan.esl.string.extensions.isUUID
import net.essentuan.esl.string.extensions.toUUID
import java.util.UUID

private const val USERNAME = "username"
private const val UUID = "uuid"
private const val VERSION = "version"

class Buster : WebSocketExtension<Buster> {
    lateinit var username: String
    lateinit var uuid: UUID
    lateinit var version: String

    val ready: Boolean
        get() = ::username.isInitialized && ::uuid.isInitialized && ::version.isInitialized

    override val factory: WebSocketExtensionFactory<Buster, out WebSocketExtension<Buster>>
        get() = Companion

    override val protocols: List<WebSocketExtensionHeader>
        get() {
            val parameters = mutableListOf(NAME)

            if (::username.isInitialized)
                parameters+= "$USERNAME=$username"

            if (::uuid.isInitialized)
                parameters+= "$UUID=$uuid"

            if (::version.isInitialized)
                parameters+= "$VERSION=$version"

            return listOf(
                WebSocketExtensionHeader(
                    parameters.joinToString(";"),
                    listOf()
                )
            )
        }

    override fun clientNegotiation(negotiatedProtocols: List<WebSocketExtensionHeader>): Boolean =
        negotiatedProtocols.any { it.name == NAME }

    override fun serverNegotiation(requestedProtocols: List<WebSocketExtensionHeader>): List<WebSocketExtensionHeader> {
        val protocol = requestedProtocols.firstOrNull { it.name == NAME }
            ?.parseParameters()
            ?.toMap() ?: return emptyList()

        protocol[USERNAME].also {
            if (it == null || !Profile.isValid(it))
                return emptyList()

            username= it
        }

        protocol[UUID].also {
            if (it?.isUUID() != true)
                return emptyList()

            uuid = it.toUUID()
        }

        protocol[VERSION].also {
            if (it != Buster.version)
                return emptyList()

            version = it
        }

        return requestedProtocols
    }

    override fun processIncomingFrame(frame: Frame): Frame = frame
    override fun processOutgoingFrame(frame: Frame): Frame = frame

    companion object : WebSocketExtensionFactory<Buster, Buster> {
        const val NAME = "buster"

        init {
            Reflections.register("com.busted_moments.buster")
        }

        val version: String
            get() = Constants.VERSION

        override fun install(config: Buster.() -> Unit): Buster =
            Buster().apply(config)

        override val key: AttributeKey<Buster> = AttributeKey(NAME)

        override val rsv1: Boolean
            get() = false
        override val rsv2: Boolean
            get() = false
        override val rsv3: Boolean
            get() = false
    }

    interface Type : Json.Model
}