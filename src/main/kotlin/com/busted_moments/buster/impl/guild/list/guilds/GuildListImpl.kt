package com.busted_moments.buster.impl.guild.list.guilds

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Guild
import com.busted_moments.buster.api.GuildType
import net.essentuan.esl.model.annotations.Root
import java.util.UUID

internal data class GuildListImpl(
    @Root
    private val guilds: Map<UUID, GuildTypeImpl>
) : Buster.Type, Guild.List {
    override val size: Int
        get() = guilds.size

    override fun isEmpty(): Boolean =
        guilds.isEmpty()

    override fun get(uuid: UUID): GuildType? =
        guilds[uuid]

    override fun get(type: GuildType): GuildType? =
        guilds[type.uuid]

    override fun contains(element: GuildType): Boolean =
        element.uuid in guilds

    override fun containsAll(elements: Collection<GuildType>): Boolean {
        for (e in elements)
            if (e !in this)
                return false

        return true
    }

    override fun iterator(): Iterator<GuildType> =
        guilds.values.iterator()
}