package com.busted_moments.buster.api

import net.essentuan.esl.encoding.JsonBasedEncoder
import net.essentuan.esl.iteration.Iterators
import net.essentuan.esl.json.Json
import net.essentuan.esl.json.json
import net.essentuan.esl.json.type.AnyJson
import net.essentuan.esl.string.extensions.toUUID
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type
import java.util.UUID

/**
 * A Wynncraft party. If a party member has not been found, their UUID will be zeroed.
 */
interface Party : Collection<Party.Member> {
    /**
     * The leader of the party. Null if there is no party.
     */
    val leader: Member?

    operator fun get(name: String): Member?

    operator fun get(uuid: UUID): Member?

    operator fun contains(name: String): Boolean =
        this[name] != null

    operator fun contains(uuid: UUID): Boolean =
        this[uuid] != null

    operator fun contains(player: PlayerType): Boolean =
        player.uuid in this || player.name in this

    override fun contains(element: Member): Boolean =
        element.uuid in this || element.name in this

    fun copy(): Party =
        this as? Impl ?: Impl(leader, this)

    interface Member : PlayerType {
        val hasUUID: Boolean
            get() = uuid.leastSignificantBits != 0L || uuid.mostSignificantBits != 0L
    }

    private data class Impl(override val leader: Member?, val members: Collection<Member>) : Party {
        private val names = mutableMapOf<String, Member>()
        private val uuids = mutableMapOf<UUID, Member>()

        init {
            for (member in members) {
                names[member.name] = member

                if (member.hasUUID)
                    uuids[member.uuid] = member
            }
        }

        override val size: Int
            get() = names.size

        override fun isEmpty(): Boolean =
            names.isEmpty()

        override fun get(name: String): Member? =
            names[name]

        override fun get(uuid: UUID): Member? =
            uuids[uuid]

        override fun containsAll(elements: Collection<Member>): Boolean {
            for (e in elements)
                if (e !in this)
                    return false

            return true
        }

        override fun iterator(): Iterator<Member> =
            members.iterator()
    }

    private data class MemberImpl(override val name: String, override val uuid: UUID) : Member

    companion object : JsonBasedEncoder<Party>() {
        private fun Member.json(): Json = json {
            "name" to name
            "uuid" to uuid.toString()
        }

        private fun AnyJson.toMember(): Member? {
            return MemberImpl(
                getString("name") ?: return null,
                getString("uuid")?.toUUID() ?: return null
            )
        }

        override fun encode(
            obj: Party,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): AnyJson? =
            json {
                "leader" to (obj.leader?.json() ?: return@json)

                "members" to obj.asSequence()
                    .filterNot { it == obj.leader }
                    .map { it.json() }
                    .toList()
            }

        override fun decode(
            obj: AnyJson,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): Party? {
            val leader = (obj["leader"] as? AnyJson)?.toMember() ?: return null

            return Impl(
                leader,
                (sequenceOf(leader).plus(
                    (obj.getList("members", AnyJson::class) ?: emptyList())
                        .asSequence()
                        .map {
                            it.toMember()
                        }
                        .filterNotNull()
                )).toList()
            )
        }
    }

    object Empty : Party {
        override val leader: Member?
            get() = null

        override val size: Int
            get() = 0

        override fun isEmpty(): Boolean =
            true

        override fun get(name: String): Member? =
            null

        override fun get(uuid: UUID): Member? =
            null

        override fun containsAll(elements: Collection<Member>): Boolean =
            elements.isEmpty()

        override fun iterator(): Iterator<Member> =
            Iterators.empty()
    }
}