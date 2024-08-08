package com.busted_moments.buster

import net.essentuan.esl.json.Json
import java.util.Date
import kotlin.random.Random
import kotlin.random.nextInt

data class Ray(
    val timestamp: Date = Date(),
    val id: Int = Random.nextInt(0..10000)
) : Json.Model