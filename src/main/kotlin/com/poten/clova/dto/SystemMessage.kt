package com.poten.clova.dto

data class SystemMessage(
    val role: String,
    val content: String
) {
    companion object {
        data class Prompt(
            val role: String,
            val content: String
        )
    }
}
