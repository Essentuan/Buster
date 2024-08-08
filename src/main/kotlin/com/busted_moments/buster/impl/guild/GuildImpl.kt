package com.busted_moments.buster.impl.guild

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Guild
import com.busted_moments.buster.api.Season
import com.busted_moments.buster.impl.guild.member.MemberImpl
import java.util.Date
import java.util.UUID

internal data class GuildImpl(
    override val uuid: UUID,
    override val name: String,
    override val tag: String,
    override val createdAt: Date,
    override val level: Int,
    override val xp: Long,
    override val required: Long,
    override val progress: Int,
    private val members: Map<UUID, MemberImpl>,
    override val contributions: ContributionsImpl,
    override val banner: BannerImpl,
    override val wars: Int,
    override val results: Season.Results,
) : Buster.Type, Guild {
    override fun get(uuid: UUID): Guild.Member? =
        members[uuid]

    override val size: Int
        get() = members.size

    override fun isEmpty(): Boolean =
        members.isEmpty()

    override fun iterator(): Iterator<Guild.Member> =
        members.values.iterator()
}