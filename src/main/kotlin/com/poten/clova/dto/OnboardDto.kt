package com.poten.clova.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.poten.clova.enum.AgeCategory

data class OnboardDto (
    val name: String,
    val age: AgeCategory?,
    val deviceId: String
)
