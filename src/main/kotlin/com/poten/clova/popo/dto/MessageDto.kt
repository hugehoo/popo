package com.poten.clova.popo.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.poten.clova.popo.entity.Message
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
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
