package com.busted_moments.buster.types.guilds

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Territory

data class TerritoryProfile(
    val name: String,
    val owner: String,
    val resources: Map<Territory.Resource, Resources>,
    val connections: MutableSet<String>,
    val defense: Territory.Rating,
    val hq: Boolean
) : Buster.Type {
    data class Resources(
        val production: Int,
        val stored: Int,
        val capacity: Int,
    ) : Buster.Type
}
