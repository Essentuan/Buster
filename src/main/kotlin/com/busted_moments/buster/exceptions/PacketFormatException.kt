package com.busted_moments.buster.exceptions

class PacketFormatException(
    cause: Throwable? = null,
    message: String? = null
) : Exception(message, cause)