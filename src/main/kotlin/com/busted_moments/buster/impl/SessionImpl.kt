package com.busted_moments.buster.impl

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Session
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.duration.ms
import net.essentuan.esl.time.span.TimeSpan
import java.util.Date

internal data class SessionImpl(
    override val start: Date,
    override val end: Date?
) : Buster.Type, Session, TimeSpan.Helper {
    override val duration: Duration
        get() = ((end?.time ?: System.currentTimeMillis()) - start.time).ms
}