package com.busted_moments.buster.api

import com.busted_moments.buster.impl.SessionImpl
import net.essentuan.esl.other.repr
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.extensions.minus
import net.essentuan.esl.time.span.TimeSpan
import java.util.Date

interface Playtime : List<Session>, Duration {
    val start: Date
        get() = firstOrNull()?.start ?: Date()
    val end: Date
        get() = lastOrNull()?.end ?: Date()

    fun between(span: TimeSpan): Playtime {
        require(span.end != null) { "TimeSpan must be bounded!" }

        var total = Duration()
        val sessions = ArrayList<Session>()

        for (i in indices) {
            val session = this[i]

            var it = if (session.end == null) SessionImpl(session.start, Date()) else session

            val startIn = it.start in span
            val endIn = it.end!! in span

            when {
                !startIn && !endIn -> continue
                startIn && !endIn -> it = SessionImpl(it.start, span.end!!)
                !startIn && endIn -> it = SessionImpl(span.start, it.end!!)
            }

            total += it
            sessions.add(it)
        }

        return View(sessions, total)
    }

    fun past(duration: Duration): Playtime =
        between(TimeSpan(Date() - duration, duration))

    companion object
}

private class View(
    private val sessions: List<Session>,
    private val duration: Duration
) : Playtime, List<Session> by sessions, Duration by duration {
    override fun toString(): String = repr {
        prefix { +"Playtime.View" }

        +::sessions
        +::duration
    }
}