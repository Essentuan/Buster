package com.busted_moments.buster.protocol

import com.busted_moments.buster.Ray

data class Response(
    val ray: Ray,
    /**
     * The payload of the response. This will always be a [String] when [status] is 1
     */
    val payload: Any,
    val status: Int,
) : Packet {
    companion object {
        const val OK = 0
        const val ERROR = 1
    }
}