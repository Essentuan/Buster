package com.busted_moments.buster.impl

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Account

internal data class AccountImpl(
    override var profile: ProfileImpl,
    override val preferences: Account.Preferences
) : Buster.Type, Account