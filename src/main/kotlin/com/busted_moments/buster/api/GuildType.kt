package com.busted_moments.buster.api

import java.util.UUID

interface GuildType {
    val uuid: UUID
    val name: String
    val tag: String

    companion object
}