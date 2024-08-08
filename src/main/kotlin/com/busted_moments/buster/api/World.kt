package com.busted_moments.buster.api

import net.essentuan.esl.other.Printable
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.extensions.timeSince
import java.util.Date
import java.util.UUID

interface World : Set<Profile>, Printable {
    val name: String
    val firstSeen: Date

    operator fun contains(uuid: UUID): Boolean
    operator fun contains(player: PlayerType): Boolean =
        player.uuid in this

    override operator fun contains(element: Profile): Boolean =
        element.uuid in this

    val age: Duration
        get() = firstSeen.timeSince()

    override fun print(): String = name

    companion object

    interface List : Set<World> {
        operator fun get(world: String): World?
        operator fun get(world: World): World?

        operator fun get(uuid: UUID): World?
        operator fun get(player: PlayerType): World?
    }
}