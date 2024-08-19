package com.poten.clova.popo.dto

data class UserMessage (
    val message: String,
    val deviceId: String,
    val character: String?
)