package com.busted_moments.buster.impl.guild

import com.busted_moments.buster.Buster
import com.busted_moments.buster.api.Guild

internal data class BannerImpl(
    override val color: Guild.Banner.Color = Guild.Banner.Color.WHITE,
    override val tier: Int = 0,
    override val structure: Guild.Banner.Structure = Guild.Banner.Structure.DEFAULT,
    private val layers: List<Layer> = emptyList()
) : Buster.Type, Guild.Banner, List<Guild.Banner.Layer> by layers {
    internal data class Layer(
        override val color: Guild.Banner.Color,
        override val pattern: Guild.Banner.Pattern
    ) : Buster.Type, Guild.Banner.Layer
}