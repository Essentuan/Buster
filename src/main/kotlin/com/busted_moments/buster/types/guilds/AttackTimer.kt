package com.busted_moments.buster.types.guilds

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Territory
import net.essentuan.esl.time.duration.Duration
import net.essentuan.esl.time.duration.seconds
import net.essentuan.esl.time.extensions.timeUntil
import java.util.Date

data class AttackTimer(
    val territory: String,
    val endsAt: Date,
    val defense: Territory.Rating,
    val trusted: Boolean
) : Buster.Type {
    val remaining: Duration
        get() = endsAt.timeUntil()

    val completed: Boolean
        get() = System.currentTimeMillis() > endsAt.time

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AttackTimer) return false

        if (territory != other.territory) return false

        val diff = (remaining - other.remaining).abs()
        if (diff < MARGIN_OF_ERROR)
            return false

        return true
    }

    override fun hashCode(): Int =
        territory.hashCode()

    override fun toString(): String {
        return "AttackTimer(territory='$territory', endsAt=$endsAt, defense=$defense, trusted=$trusted)"
    }

    companion object {
        val MARGIN_OF_ERROR = 20.seconds
    }
}
