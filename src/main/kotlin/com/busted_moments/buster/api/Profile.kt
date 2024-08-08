package com.busted_moments.buster.api

import net.essentuan.esl.time.span.TimeSpan

interface Profile : PlayerType, List<Profile.Entry> {
    operator fun contains(name: String): Boolean =
        any { it.name == name }

    val playtime: Playtime

    interface Entry : TimeSpan {
        val name: String
    }

    companion object {
        /**
         * @return true if [char] is allowed in a Minecraft username.
         */
        private fun isValid(char: Char): Boolean = when (char) {
            '_' -> true
            in 'a'..'z' -> true
            in 'A'..'Z' -> true
            in '0'..'9' -> true
            else -> false
        }

        /**
         * @return true if [string] is a valid Minecraft username.
         */
        fun isValid(string: String): Boolean {
            if (string.length > 16)
                return false

            for (c in string)
                if (!isValid(c))
                    return false

            return true
        }
    }
}