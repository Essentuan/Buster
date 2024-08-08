package com.busted_moments.buster.impl.guild.member

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Guild
import com.busted_moments.buster.api.GuildType
import com.busted_moments.buster.impl.ProfileImpl
import java.util.Date
import java.util.UUID

internal data class MemberImpl(
    override val profile: ProfileImpl,
    private val history: MutableList<EntryImpl> = mutableListOf()
) : Buster.Type, Guild.Member, List<Guild.Member.Entry> by history {
    override val name: String
        get() = profile.name
    override val uuid: UUID
        get() = profile.uuid

    private val current: EntryImpl?
        get() {
            val entry = history.lastOrNull() ?: return null

            if (entry.end != null)
                return null

            return entry
        }

    override val guild: GuildType?
        get() = current

    override val rank: Guild.Rank?
        get() = current?.lastOrNull()?.rank
    override val joinedAt: Date?
        get() = current?.start
    override val contributed: Long?
        get() = current?.contributions?.total
}