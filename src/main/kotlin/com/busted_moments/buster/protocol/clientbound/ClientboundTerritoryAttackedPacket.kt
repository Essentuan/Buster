package com.busted_moments.buster.protocol.clientbound

import com.busted_moments.buster.protocol.Packet
import com.busted_moments.buster.types.guilds.AttackTimer

class ClientboundTerritoryAttackedPacket(
    val timer: AttackTimer
) : Packet