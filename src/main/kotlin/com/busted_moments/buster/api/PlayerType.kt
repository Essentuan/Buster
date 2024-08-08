package com.busted_moments.buster.api

import java.util.UUID

interface PlayerType {
    val name: String
    val uuid: UUID

    companion object
}