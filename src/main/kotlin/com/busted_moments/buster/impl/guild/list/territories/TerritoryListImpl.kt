package com.busted_moments.buster.impl.guild.list.territories

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Territory
import java.util.Date
import java.util.UUID

internal data class TerritoryListImpl(
    private val territories: Map<String, TerritoryImpl>,
    override val timestamp: Date
) : Buster.Type, Territory.List {
    override val guilds: Map<UUID, Collection<Territory>> = groupBy { it.owner.uuid }

    override val size: Int
        get() = territories.size

    override fun isEmpty(): Boolean =
        territories.isEmpty()

    override fun get(territory: String): Territory? =
        territories[territory]

    override fun get(territory: Territory): Territory? =
        territories[territory.name]

    override fun contains(element: Territory): Boolean =
        element.name in territories

    override fun containsAll(elements: Collection<Territory>): Boolean {
        for (e in elements)
            if (e !in this)
                return false

        return true
    }

    override fun iterator(): Iterator<Territory> =
        territories.values.iterator()
}