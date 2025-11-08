package com.busted_moments.buster.protocol.clientbound

import com.busted_moments.buster.protocol.Packet
import net.essentuan.esl.time.duration.Duration

data class ClientboundSetAttackTimerMarginOfErrorPacket(
    val marginOfError: Duration
) : Packet
