package com.busted_moments.buster.api

import net.essentuan.esl.collections.maps.IntMap
import net.essentuan.esl.encoding.AbstractEncoder
import net.essentuan.esl.encoding.JsonBasedEncoder
import net.essentuan.esl.encoding.StringBasedEncoder
import net.essentuan.esl.encoding.builtin.EnumEncoder
import net.essentuan.esl.json.Json
import net.essentuan.esl.json.json
import net.essentuan.esl.json.type.AnyJson
import net.essentuan.esl.other.Printable
import net.essentuan.esl.other.unsupported
import net.essentuan.esl.reflections.extensions.simpleString
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.extensions.minus
import net.essentuan.esl.time.span.TimeSpan
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Type
import java.util.Date
import java.util.UUID
import kotlin.math.pow

typealias IList<T> = List<T>

interface Guild : GuildType, Collection<Guild.Member> {
    val createdAt: Date
    val owner: Member
        get() = first { it.rank == Rank.OWNER }

    operator fun get(uuid: UUID): Member?
    operator fun get(player: PlayerType): Member? =
        this[player.uuid]

    operator fun contains(uuid: UUID): Boolean =
        this[uuid] != null

    operator fun contains(player: PlayerType): Boolean =
        player.uuid in this

    override fun contains(element: Member): Boolean =
        element.uuid in this

    override fun containsAll(elements: Collection<Member>): Boolean {
        for (e in elements)
            if (e !in this)
                return false

        return true
    }

    val level: Int
    val xp: Long
    val required: Long
    val progress: Int

    val contributions: Contributions

    val banner: Banner

    val wars: Int
    val results: Season.Results

    interface Member : IList<Member.Entry>, PlayerType {
        val profile: Profile

        val guild: GuildType?

        val rank: Rank?
        val joinedAt: Date?
        val contributed: Long?

        interface Entry : GuildType, IList<Status>, TimeSpan {
            val contributions: Contributions
        }

        interface Status : TimeSpan {
            val rank: Rank
        }

        companion object
    }

    interface List : Set<GuildType> {
        operator fun get(uuid: UUID): GuildType?

        operator fun get(type: GuildType): GuildType?
    }

    enum class Rank : Printable, Comparable<Rank> {
        RECRUIT,
        RECRUITER,
        CAPTAIN,
        STRATEGIST,
        CHIEF,
        OWNER;

        private val friendly: String = name[0].uppercase() + name.substring(1).lowercase()

        val stars: Int
            get() = ordinal

        override fun print(): String = friendly
    }

    interface Banner : IList<Banner.Layer> {
        val color: Color
        val tier: Int
        val structure: Structure

        interface Layer {
            val color: Color
            val pattern: Pattern
        }

        enum class Structure : Printable {
            DEFAULT,
            UNKNOWN,
            DERNIC_BANNER,
            LIGHT_BANNER,
            CORRUPTED_BANNER,
            MOLTEN_BANNER,
            DESERT_BANNER,
            FUTURISTIC_BANNER,
            STEAMPUNK_BANNER,
            NATURE_BANNER,
            DECAY_BANNER,
            ICE_BANNER,
            HIVE_BANNER,
            JESTER_BANNER,
            BEACHSIDE_BANNER,
            OTHERWORLDLY_BANNER,
            DYNASTY_BANNER,
            PIRATE_BANNER,
            HARVEST_BANNER,
            IMPERIAL_BANNER;

            private val friendly: String = name[0].uppercase() + name.substring(1).lowercase()

            override fun print(): String = friendly

            object Encoder : StringBasedEncoder<Structure>() {
                override fun encode(
                    obj: Structure,
                    flags: Set<Any>,
                    type: Class<*>,
                    element: AnnotatedElement,
                    vararg typeArgs: Type
                ): String =
                    obj.toString()

                override fun decode(
                    obj: String,
                    flags: Set<Any>,
                    type: Class<*>,
                    element: AnnotatedElement,
                    vararg typeArgs: Type
                ): Structure? {
                    if (obj.startsWith("tier"))
                        return DEFAULT

                    return try {
                        EnumEncoder.decode(obj, flags, type, element, *typeArgs) as Structure
                    } catch (_: Exception) {
                        UNKNOWN
                    }
                }

            }
        }

        enum class Pattern : Printable {
            STRIPE_BOTTOM,
            STRIPE_TOP,
            STRIPE_LEFT,
            STRIPE_RIGHT,
            STRIPE_MIDDLE,
            STRIPE_CENTER,
            STRIPE_DOWNRIGHT,
            STRIPE_DOWNLEFT,
            STRIPE_SMALL,
            CROSS,
            STRAIGHT_CROSS,
            DIAGONAL_LEFT,
            DIAGONAL_RIGHT_MIRROR,
            DIAGONAL_LEFT_MIRROR,
            DIAGONAL_RIGHT,
            HALF_VERTICAL,
            HALF_VERTICAL_MIRROR,
            HALF_HORIZONTAL,
            HALF_HORIZONTAL_MIRROR,
            SQUARE_BOTTOM_LEFT,
            SQUARE_BOTTOM_RIGHT,
            SQUARE_TOP_LEFT,
            SQUARE_TOP_RIGHT,
            TRIANGLE_BOTTOM,
            TRIANGLE_TOP,
            TRIANGLES_BOTTOM,
            TRIANGLES_TOP,
            CIRCLE_MIDDLE,
            RHOMBUS_MIDDLE,
            BORDER,
            CURLY_BORDER,
            BRICKS,
            GRADIENT,
            GRADIENT_UP,
            CREEPER,
            SKULL,
            FLOWER,
            MOJANG,
            GLOBE,
            PIGLIN;

            private val friendly: String = name[0].uppercase() + name.substring(1).lowercase()

            override fun print(): String = friendly
        }

        enum class Color : Printable {
            WHITE,
            ORANGE,
            MAGENTA,
            LIGHT_BLUE,
            YELLOW,
            LIME,
            PINK,
            GRAY,
            SILVER,
            CYAN,
            PURPLE,
            BLUE,
            BROWN,
            GREEN,
            RED,
            BLACK;

            private val friendly: String = name[0].uppercase() + name.substring(1).lowercase()

            override fun print(): String = friendly
        }
    }

    companion object {
        private var levels: DoubleArray = DoubleArray(150)

        fun required(level: Int): Double {
            if (level <= 0)
                return 0.0

            require(level <= 150) { "Max level is 150!" }

            val xp = levels[level - 1]
            if (xp != 0.0)
                return xp

            return required(level - 1) + (20000 * 1.15.pow(level - 1.0))
                .also {
                    levels[level - 1] = it
                }
        }
    }

    object Names {
        /**
         * @return true if [char] is allowed in a [Guild.name]
         */
        fun isValid(char: Char): Boolean = when (char) {
            ' ' -> true
            in 'a'..'z' -> true
            in 'A'..'Z' -> true
            else -> false
        }

        /**
         * @return true if [string] is a valid [Guild.name]
         */
        fun isValid(string: String): Boolean {
            if (string.length !in 4..30)
                return false

            for (c in string)
                if (!isValid(c))
                    return false

            return true
        }
    }

    object Tags {
        /**
         * @return true if [char] is allowed in a [Guild.tag]
         */
        fun isValid(char: Char): Boolean = when (char) {
            in 'a'..'z' -> true
            in 'A'..'Z' -> true
            else -> false
        }

        /**
         * @return true if [string] is a valid [Guild.tag]
         */
        fun isValid(string: String): Boolean {
            if (string.length !in 3..4)
                return false

            for (c in string)
                if (!isValid(c))
                    return false

            return true
        }
    }
}

interface Contribution {
    val at: Date
    val amount: Long

    operator fun component1(): Date = at

    operator fun component2(): Long = amount

    companion object {
        operator fun invoke(at: Date, amount: Long): Contribution =
            ContributionImpl(at, amount)

        operator fun invoke(json: AnyJson): Contribution =
            Contribution(
                json.getDate("at")!!,
                json.getLong("amount")!!
            )
    }

    object Encoder : JsonBasedEncoder<Contribution>() {
        override fun decode(
            obj: AnyJson,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): Contribution = Contribution(obj)

        override fun encode(
            obj: Contribution,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): AnyJson = json {
            "at" to obj.at.time
            "amount" to obj.amount
        }
    }
}

interface Contributions : List<Contribution> {
    val total: Long

    fun between(span: TimeSpan): Contributions

    fun past(duration: Duration): Contributions

    interface Helper : Contributions {
        val entries: List<Contribution>

        override fun between(span: TimeSpan): Contributions {
            if (isEmpty())
                return EMPTY_CONTRIBUTIONS

            var start: Int? = null

            for (i in entries.indices) {
                val contribution = entries[i]

                if (contribution.at !in span) {
                    return SubContributions(
                        entries.subList(start ?: return EMPTY_CONTRIBUTIONS, i)
                    )
                } else if (start == null)
                    start = i
            }

            return SubContributions(
                entries.subList(start ?: return EMPTY_CONTRIBUTIONS, entries.size)
            )
        }

        override fun past(duration: Duration): Contributions =
            between(TimeSpan(Date() - duration, duration))

        override val size: Int
            get() = entries.size

        override fun contains(element: Contribution): Boolean =
            entries.contains(element)

        override fun containsAll(elements: Collection<Contribution>): Boolean =
            entries.containsAll(elements)

        override fun get(index: Int): Contribution =
            entries[index]

        override fun indexOf(element: Contribution): Int =
            entries.indexOf(element)

        override fun isEmpty(): Boolean =
            entries.isEmpty()

        override fun iterator(): Iterator<Contribution> =
            entries.iterator()

        override fun lastIndexOf(element: Contribution): Int =
            entries.lastIndexOf(element)

        override fun listIterator(): ListIterator<Contribution> =
            entries.listIterator()

        override fun listIterator(index: Int): ListIterator<Contribution> =
            entries.listIterator(index)

        override fun subList(fromIndex: Int, toIndex: Int): List<Contribution> =
            entries.subList(fromIndex, toIndex)
    }
}

private val EMPTY_CONTRIBUTIONS = SubContributions(emptyList())

private data class SubContributions(
    override val entries: List<Contribution>
) : Contributions.Helper {
    override val total: Long = entries.sumOf { it.amount }
}

private class ContributionImpl(
    override val at: Date,
    override val amount: Long
) : Contribution {
    override fun equals(other: Any?): Boolean {
        if (other !is Contribution)
            return false

        return this === other || (other.at == at && other.amount == amount)
    }

    override fun hashCode(): Int {
        var result = at.hashCode()
        result = 31 * result + amount.hashCode()
        return result
    }

    override fun toString(): String {
        return "Contribution[at=$at, amount=$amount]"
    }
}

interface Season {
    val rating: Long
    val territories: Int

    operator fun component1(): Long = rating

    operator fun component2(): Int = territories

    companion object : JsonBasedEncoder<Season>() {
        operator fun invoke(rating: Long, territories: Int): Season {
            return if (rating == 0L && territories == 0)
                EmptySeason
            else
                SeasonImpl(rating, territories)
        }

        operator fun invoke(json: AnyJson): Season {
            return this(
                json.getLong("rating", 0),
                json.getInteger("finalTerritories", 0)
            )
        }

        operator fun invoke(): Season = EmptySeason

        override fun decode(
            obj: AnyJson,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): Season =
            Season(obj)

        override fun encode(
            obj: Season,
            flags: Set<Any>,
            type: Class<*>,
            element: AnnotatedElement,
            vararg typeArgs: Type
        ): AnyJson =
            json {
                "rating" to obj.rating
                "finalTerritories" to obj.territories
            }
    }

    interface Results : Collection<Season> {
        operator fun get(season: Int): Season

        operator fun contains(season: Int): Boolean

        companion object : AbstractEncoder<Results, Any>() {
            operator fun invoke(results: List<Json?>): Results =
                ResultsImpl(results.size).also {
                    for (i in results.indices)
                        it[i] = Season(results[i] ?: continue)
                }

            operator fun invoke(json: Json): Results = ResultsImpl(json.size).also {
                for (entry in json.entries)
                    it[entry.key.toInt()] = Season(entry.asJson() ?: continue)
            }

            operator fun invoke(): Results = EmptyResults

            @Suppress("UNCHECKED_CAST")
            override fun decode(
                obj: Any,
                flags: Set<Any>,
                type: Class<*>,
                element: AnnotatedElement,
                vararg typeArgs: Type
            ): Results = when (obj) {
                is Json -> Results(obj)
                is List<*> -> Results(obj as List<Json?>)
                else -> throw IllegalArgumentException("Unsupported type ${obj.javaClass.simpleString()}!")
            }

            override fun encode(
                obj: Results,
                flags: Set<Any>,
                type: Class<*>,
                element: AnnotatedElement,
                vararg typeArgs: Type
            ): Any = obj.map {
                json {
                    "rating" to it.rating
                    "finalTerritories" to it.territories
                }
            }

            override fun toString(
                obj: Results,
                flags: Set<Any>,
                type: Class<*>,
                element: AnnotatedElement,
                vararg typeArgs: Type
            ): String = unsupported()

            override fun valueOf(
                string: String,
                flags: Set<Any>,
                type: Class<*>,
                element: AnnotatedElement,
                vararg typeArgs: Type
            ): Results = unsupported()
        }
    }
}

private const val EMPTY_HASH = 961

private object EmptySeason : Season {
    override val rating: Long
        get() = 0
    override val territories: Int
        get() = 0

    override fun hashCode(): Int {
        return EMPTY_HASH
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Season)
            return false

        if (other === EmptySeason)
            return true

        return other.rating == 0L && other.territories == 0
    }

    override fun toString(): String {
        return "Season[rating=0, territories=0]"
    }
}

private class SeasonImpl(
    override val rating: Long,
    override val territories: Int
) : Season {
    override fun equals(other: Any?): Boolean {
        if (other !is Season)
            return false

        return this === other || (other.rating == rating && other.territories == territories)
    }

    override fun hashCode(): Int {
        var result = rating.hashCode()
        result = 31 * result + territories
        return result
    }

    override fun toString(): String {
        return "Season[rating=$rating, territories=$territories]"
    }
}

private open class ResultsImpl(capacity: Int = 8) : IntMap<Season>(capacity), Season.Results {
    override fun get(season: Int): Season =
        super.get(season) ?: EmptySeason

    override fun contains(season: Int): Boolean =
        super.containsKey(season)

    override fun contains(element: Season): Boolean =
        super.containsValue(element)

    override fun containsAll(elements: Collection<Season>): Boolean =
        values.containsAll(elements)

    override fun iterator(): kotlin.collections.Iterator<Season> =
        values.iterator()
}

private object EmptyResults : ResultsImpl(0)