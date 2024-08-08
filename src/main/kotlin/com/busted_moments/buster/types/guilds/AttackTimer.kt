package com.busted_moments.buster.types.guilds

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Territory
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.extensions.timeUntil
import java.util.Date

private const val TEN_SECONDS = 10000L

data class AttackTimer(
    val territory: String,
    val endsAt: Date,
    val defense: Territory.Rating,
    val trusted: Boolean
) : Buster.Type {
    val id: String = java.lang.Long.toHexString(endsAt.time / TEN_SECONDS)

    val remaining: Duration
        get() = endsAt.timeUntil()

    val completed: Boolean
        get() = System.currentTimeMillis() > endsAt.time

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AttackTimer) return false

        if (territory != other.territory) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = territory.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    override fun toString(): String {
        return "AttackTimer(id='$id', territory='$territory', endsAt=$endsAt, defense=$defense, trusted=$trusted)"
    }
}