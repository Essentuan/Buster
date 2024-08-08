package com.busted_moments.buster.protocol.requests

import com.busted_moments.buster.api.Guild
import com.busted_moments.buster.impl.guild.GuildImpl
import com.busted_moments.buster.protocol.Request
import com.busted_moments.buster.protocol.Response
import net.essentuan.esl.json.Json
import net.essentuan.esl.model.Model.Companion.wrap

class GuildRequest(
    val guild: String
) : Request<Guild>() {
    override fun wrap(response: Response): Guild? {
        return (response.payload as? Json)?.wrap(GuildImpl::class)
    }
}