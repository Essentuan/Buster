package com.busted_moments.buster.impl.worlds

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.PlayerType
import com.busted_moments.buster.api.World
import net.essentuan.esl.model.annotations.Ignored
import net.essentuan.esl.model.annotations.Root
import java.util.UUID

internal data class WorldListImpl(
    @Root
    private val worlds: Map<String, WorldImpl>
) : Buster.Type, World.List {
    @Ignored
    private val players = worlds.asSequence()
        .flatMap { (_, world) ->
            world.map {
                it.uuid to world
            }
        }.toMap()

    override val size: Int
        get() = worlds.size

    override fun isEmpty(): Boolean =
        worlds.isEmpty()


    override fun get(world: String): World? =
        worlds[world]

    override fun get(world: World): World? =
        worlds[world.name]

    override fun get(uuid: UUID): World? =
        players[uuid]

    override fun get(player: PlayerType): World? =
        players[player.uuid]

    override fun contains(element: World): Boolean =
        element.name in worlds

    override fun containsAll(elements: Collection<World>): Boolean {
        for (world in elements)
            if (world !in this)
                return false

        return true
    }

    override fun iterator(): Iterator<World> =
        worlds.values.iterator()
}