package com.busted_moments.buster.protocol.clientbound

import com.busted_moments.buster.api.Territory
import com.busted_moments.buster.impl.guild.list.territories.TerritoryListImpl
import com.busted_moments.buster.protocol.Packet
import net.essentuan.esl.json.Json
import net.essentuan.esl.model.Model.Companion.wrap

data class ClientboundMapPacket(val data: Json) : Packet {
    fun wrap(): Territory.List =
        data.wrap(TerritoryListImpl::class)
}