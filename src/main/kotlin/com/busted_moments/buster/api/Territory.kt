package com.busted_moments.buster.api

import net.essentuan.esl.other.Printable
import java.util.Date
import java.util.UUID

interface Territory {
    val name: String
    val owner: GuildType
    val acquired: Date
    val location: Location
    val resources: Map<Resource, Storage>
    val defense: Rating
    val treasury: Rating
    val hq: Boolean

    val connections: Set<String>

    interface Location {
        val start: Pos
        val end: Pos
    }

    interface Pos {
        val x: Int
        val z: Int

        operator fun component1() = x
        operator fun component2() = x
    }

    interface Storage {
        val base: Int
        val production: Int

        val stored: Int
        val capacity: Int
    }


    enum class Resource : Printable {
        EMERALDS,
        ORE,
        WOOD,
        FISH,
        CROP;

        private val friendly: String = name[0].uppercase() + name.substring(1).lowercase()

        override fun print(): String = friendly
    }

    enum class Rating : Printable {
        VERY_LOW,
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH;

        private val friendly: String = name.split("_").joinToString(" ") {
            it[0].uppercase() + it.substring(1).lowercase()
        }

        override fun print(): String = friendly
    }

    interface List : Collection<Territory> {
        val guilds: Map<UUID, Collection<Territory>>
        val timestamp: Date

        operator fun get(territory: String): Territory?
        operator fun get(territory: Territory): Territory?

        operator fun contains(territory: String): Boolean =
            get(territory) != null
    }
}