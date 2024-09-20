package com.busted_moments.buster.protocol.requests

import com.busted_moments.buster.protocol.Request
import com.busted_moments.buster.protocol.Response

class PingRequest : Request<Unit>() {
    override fun wrap(response: Response) = Unit
}