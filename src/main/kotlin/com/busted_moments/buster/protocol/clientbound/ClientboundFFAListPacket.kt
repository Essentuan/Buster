package com.busted_moments.buster.protocol.clientbound

import com.busted_moments.buster.protocol.Packet

data class ClientboundFFAListPacket(
    val ffas: Set<String>
) : Packet {
}