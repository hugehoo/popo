package com.poten.clova.dto

import com.poten.clova.enum.AgeCategory


data class Onboard (
    val name: String,
    val age: AgeCategory,
    val deviceId: String
)
