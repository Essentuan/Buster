package com.busted_moments.buster.protocol.clientbound

import com.busted_moments.buster.api.World
import com.busted_moments.buster.impl.worlds.WorldListImpl
import com.busted_moments.buster.protocol.Packet
import net.essentuan.esl.json.Json
import net.essentuan.esl.model.Model.Companion.wrap

data class ClientboundWorldListPacket(val data: Json) : Packet {
    fun wrap(): World.List =
        data.wrap(WorldListImpl::class)
}