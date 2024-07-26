package com.poten.clova.dto

data class UserMessage (
    val message: String,
    val deviceId: String,
    val character: String?
)