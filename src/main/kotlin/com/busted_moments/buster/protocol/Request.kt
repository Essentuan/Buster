package com.busted_moments.buster.protocol

import com.busted_moments.buster.Ray

abstract class Request<T> : Packet {
    var ray: Ray = Ray()
        private set

    abstract fun wrap(response: Response): T?
}