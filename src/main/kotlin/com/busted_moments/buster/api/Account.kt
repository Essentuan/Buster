package com.busted_moments.buster.api

import com.busted_moments.buster.Buster

interface Account {
    val profile: Profile
    val preferences: Preferences

    data class Preferences(
        var private: Boolean = false
    ) : Buster.Type

    companion object
}