package com.busted_moments.buster.impl.guild.member

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Guild
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.duration.ms
import net.essentuan.esl.time.span.TimeSpan
import java.util.Date

internal data class StatusImpl(
    override var rank: Guild.Rank,
    override var start: Date = Date(),
    override val end: Date? = null
) : Buster.Type, Guild.Member.Status, TimeSpan.Helper {
    override val duration: Duration
        get() = ((end?.time ?: System.currentTimeMillis()) - start.time).ms
}