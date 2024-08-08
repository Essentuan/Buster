package com.busted_moments.buster.impl.worlds

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Profile
import com.busted_moments.buster.api.World
import com.busted_moments.buster.impl.ProfileImpl
import java.util.Date
import java.util.UUID

internal data class WorldImpl(
    override val name: String,
    override val firstSeen: Date,
    private val players: MutableMap<UUID, ProfileImpl> = mutableMapOf()
) : Buster.Type, World {
    override val size: Int
        get() = players.size

    override fun isEmpty(): Boolean = players.isEmpty()

    override fun contains(uuid: UUID): Boolean = uuid in players

    override fun containsAll(elements: Collection<Profile>): Boolean {
        for (profile in elements)
            if (profile !in this)
                return false

        return true
    }

    override fun iterator(): Iterator<Profile> = players.values.iterator()
}