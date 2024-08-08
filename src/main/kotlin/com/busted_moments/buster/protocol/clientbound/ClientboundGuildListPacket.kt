package com.busted_moments.buster.protocol.clientbound

import com.busted_moments.buster.api.Guild
import com.busted_moments.buster.impl.guild.list.guilds.GuildListImpl
import com.busted_moments.buster.protocol.Packet
import net.essentuan.esl.json.Json
import net.essentuan.esl.model.Model.Companion.wrap

data class ClientboundGuildListPacket(val data: Json) : Packet {
    fun wrap(): Guild.List =
        data.wrap(GuildListImpl::class)
}