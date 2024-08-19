package com.poten.clova.popo.dto

data class ChatCompletionRequest(
    val messages: List<SystemMessage>,
    val topP: Double = 0.8,
    val topK: Int = 0,
    val maxTokens: Int = 256,
    val temperature: Double = 0.47,
    val repeatPenalty: Double = 6.1,
    val stopBefore: List<String> = listOf(),
    val includeAiFilters: Boolean = true,
    val seed: Int = 0
)

