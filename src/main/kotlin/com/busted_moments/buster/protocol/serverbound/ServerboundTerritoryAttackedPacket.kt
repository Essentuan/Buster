package com.busted_moments.buster.protocol.serverbound

import com.busted_moments.buster.protocol.Packet
import com.busted_moments.buster.types.guilds.AttackTimer

data class ServerboundTerritoryAttackedPacket(
    val timer: AttackTimer
) : Packet