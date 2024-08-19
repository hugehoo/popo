package com.poten.clova.popo.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.poten.clova.popo.enum.AgeCategory

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Onboard (
    val name: String,
    val age: AgeCategory?,
    val deviceId: String
)
