package com.busted_moments.buster.protocol.serverbound

import com.busted_moments.buster.protocol.Packet
import com.busted_moments.buster.types.guilds.TerritoryProfile

class ServerboundTerritoryProfileUpdatePacket(
    val world: String,
    val profiles: Map<String, TerritoryProfile>
) : Packet