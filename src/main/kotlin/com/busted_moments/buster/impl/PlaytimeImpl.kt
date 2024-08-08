package com.busted_moments.buster.impl

import com.busted_moments.buster.api.Playtime
import com.busted_moments.buster.api.Session
import net.essentuan.esl.encoding.AbstractEncoder
import net.essentuan.esl.json.Json
import net.essentuan.esl.model.Model
import net.essentuan.esl.model.Model.Companion.export
import net.essentuan.esl.other.unsupported
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.duration.seconds
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type

internal data class PlaytimeImpl(
    private val sessions: List<SessionImpl>,
    private val total: Duration
) : Playtime, List<Session> by sessions, Duration by total {
    companion object : AbstractEncoder<PlaytimeImpl, List<Json>>() {
        private val SESSION_DESCRIPTOR = Model.descriptor(SessionImpl::class.java)

        override fun decode(
            obj: List<Json>,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): PlaytimeImpl {
            var total = 0.seconds

            return PlaytimeImpl(
                obj.map { SESSION_DESCRIPTOR(it, setOf()).also { session -> total+= session } },
                total
            )
        }

        override fun encode(
            obj: PlaytimeImpl,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): List<Json> =
            obj.sessions.map { it.export() }

        override fun toString(
            obj: PlaytimeImpl,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): String = unsupported()

        override fun valueOf(
            string: String,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): PlaytimeImpl = unsupported()

    }
}