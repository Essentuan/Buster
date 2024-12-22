package com.busted_moments.buster.protocol.serverbound

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Party
import com.busted_moments.buster.protocol.Packet
import java.util.Date

class ServerboundContentCompletion(
    val id: String,
    val name: String,
    val party: Party,
    val start: Date,
    val stages: List<ContentStage>,
    val end: Date,
    val modifiers: Set<ContentModifier>
) : Packet

data class ContentStage(
    val name: String,
    val start: Date,
    val end: Date
) : Buster.Type

enum class ContentModifier {
    GUILD_RAID
}