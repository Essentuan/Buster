package com.busted_moments.buster.impl.guild.member

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Guild
import com.busted_moments.buster.impl.guild.ContributionsImpl
import net.essentuan.esl.model.annotations.ReadOnly
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.duration.ms
import net.essentuan.esl.time.span.TimeSpan
import java.util.Date
import java.util.UUID

internal data class EntryImpl(
    override val uuid: UUID,
    override val name: String,
    override val tag: String,
    override val start: Date,
    override val contributions: ContributionsImpl,
    private val status: MutableList<StatusImpl>,
    @property:ReadOnly
    override var end: Date? = null
) : Buster.Type, Guild.Member.Entry, TimeSpan.Helper, List<Guild.Member.Status> by status {
    override val duration: Duration
        get() = ((end?.time ?: System.currentTimeMillis()) - start.time).ms
}