package com.poten.clova.dto

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


data class ApiResponse(
    val status: Status,
    val result: Result
) {
    data class Status(
        val code: String,
        val message: String
    )

    data class Result(
        val message: Message,
        val inputLength: Int,
        val outputLength: Int,
        val stopReason: String,
        val seed: Long,
        val aiFilter: List<AiFilter>
    )

    data class Message(
        val role: String,
        val content: String
    )

    data class AiFilter(
        val groupName: String,
        val name: String,
        val score: String,
        val result: String
    )

    companion object {
        fun fromJson(json: String): ApiResponse {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(json, ApiResponse::class.java)
        }


        data class CharmMessage(val four_idioms: String, val message: String) {
            companion object {
                fun getCharmMessage(json: String): CharmMessage {
                    val mapper = jacksonObjectMapper()
                    return mapper.readValue(json, CharmMessage::class.java)
                }
            }
        }


    }
}
