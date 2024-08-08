package com.busted_moments.buster.impl.guild

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Contribution
import com.busted_moments.buster.api.Contributions

internal data class ContributionsImpl(
    override val entries: MutableList<Contribution>,
    override val total: Long
) : Buster.Type, Contributions.Helper