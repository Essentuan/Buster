package com.busted_moments.buster.protocol.requests

import com.busted_moments.buster.api.Guild
import com.busted_moments.buster.impl.guild.member.MemberImpl
import com.busted_moments.buster.protocol.Request
import com.busted_moments.buster.protocol.Response
import net.essentuan.esl.json.Json
import net.essentuan.esl.model.Model.Companion.wrap

class MemberRequest(
    val member: String
) : Request<Guild.Member>() {
    override fun wrap(response: Response): Guild.Member? =
        (response.payload as? Json)?.wrap(MemberImpl::class)
}