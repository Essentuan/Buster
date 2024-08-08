package com.busted_moments.buster.protocol.requests

import com.busted_moments.buster.api.Account
import com.busted_moments.buster.impl.AccountImpl
import com.busted_moments.buster.protocol.Request
import com.busted_moments.buster.protocol.Response
import net.essentuan.esl.json.Json
import net.essentuan.esl.model.Model.Companion.wrap

class AuthRequest : Request<Account>() {
    override fun wrap(response: Response): Account? {
        return (response.payload as? Json)?.wrap(AccountImpl::class)
    }
}