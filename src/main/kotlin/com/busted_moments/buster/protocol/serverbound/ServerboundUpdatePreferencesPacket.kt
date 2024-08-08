package com.busted_moments.buster.protocol.serverbound

import com.busted_moments.buster.api.Account
import com.busted_moments.buster.protocol.Packet

data class ServerboundUpdatePreferencesPacket(val preferences: Account.Preferences) : Packet