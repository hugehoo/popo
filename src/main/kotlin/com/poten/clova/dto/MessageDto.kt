package com.poten.clova.dto

import com.poten.clova.entity.Message
import java.time.LocalDate

data class MessageDto(
    val id: Long = 0L,
    val userMood: String,
    val clovaMood: String,
    val character: String,
    val date: LocalDate,
) {
    companion object {
        fun by(message: Message) = MessageDto(
            id = message.id,
            userMood = message.userMood,
            clovaMood = message.clovaMood,
            character = message.character,
            date = message.createdAt.toLocalDate()
        )
    }
}
