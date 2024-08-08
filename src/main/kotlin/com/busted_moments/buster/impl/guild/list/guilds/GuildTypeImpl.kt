package com.busted_moments.buster.impl.guild.list.guilds

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.GuildType
import java.util.UUID

internal data class GuildTypeImpl(
    override val uuid: UUID,
    override val name: String,
    override val tag: String
) : Buster.Type, GuildType