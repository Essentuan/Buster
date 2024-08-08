package com.busted_moments.buster.impl

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Profile
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.span.TimeSpan
import java.util.Date
import java.util.UUID

internal data class ProfileImpl(
    override val name: String,
    override val uuid: UUID,
    override val playtime: PlaytimeImpl,
    private val history: List<Name>
) : Buster.Type, Profile, List<Profile.Entry> by history {
    data class Name(
        override val name: String,
        override val start: Date,
        override val end: Date?
    ) : Buster.Type, Profile.Entry, TimeSpan.Helper {
        override val duration: Duration
            get() = Duration(start, end ?: Date())

    }
}