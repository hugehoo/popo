package com.poten.clova.popo.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ClovaMessage (
    val clova_mood: String,
    val character: String,
)
