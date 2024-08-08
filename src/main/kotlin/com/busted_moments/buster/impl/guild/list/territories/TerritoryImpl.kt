package com.busted_moments.buster.impl.guild.list.territories

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.GuildType
import com.busted_moments.buster.api.Territory
import net.essentuan.esl.time.duration.days
import net.essentuan.esl.time.duration.hours
import net.essentuan.esl.time.extensions.timeSince
import java.util.Date
import java.util.UUID

internal data class TerritoryImpl(
    override val name: String,
    override val acquired: Date,
    override val owner: OwnerImpl,
    override val location: LocationImpl,
    override val resources: Map<Territory.Resource, StorageImpl>,
    override val defense: Territory.Rating,
    override val hq: Boolean,
    override val connections: Set<String>
) : Buster.Type, Territory {
    internal data class OwnerImpl(
        override val name: String,
        override val tag: String,
        override val uuid: UUID,
    ) : Buster.Type, GuildType

    internal data class LocationImpl(
        override val start: PosImpl,
        override val end: PosImpl
    ) : Buster.Type, Territory.Location

    internal data class PosImpl(
        override val x: Int,
        override val z: Int
    ): Buster.Type, Territory.Pos

    internal data class StorageImpl(
        override val base: Int,
        override val production: Int,
        override val stored: Int,
        override val capacity: Int
    ) : Buster.Type, Territory.Storage

    override val treasury: Territory.Rating
        get() {
            val held = acquired.timeSince()

            return when {
                held >= 12.days -> Territory.Rating.VERY_HIGH
                held >= 5.days -> Territory.Rating.HIGH
                held >= 1.days -> Territory.Rating.MEDIUM
                held >= 1.hours -> Territory.Rating.LOW
                else -> Territory.Rating.VERY_LOW
            }
        }
}