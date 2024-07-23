package com.poten.clova.service

import com.poten.clova.dto.ApiResponse
import com.poten.clova.dto.ChatCompletionRequest
import com.poten.clova.dto.SystemMessage
import com.poten.clova.dto.UserMessage
import com.poten.clova.entity.Message
import com.poten.clova.repository.MessageRepository
import com.poten.clova.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class ClovaService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val clovaServiceClient: ClovaServiceClient
) {
    @Value("\${spring.clova.apiKey}")
    lateinit var apiKey: String

    @Value("\${spring.clova.apiGwKey}")
    lateinit var apiGwKey: String

    @Value("\${spring.clova.requestId}")
    lateinit var requestId: String

    /**
     * 1. user message input
     * 2. send to clova
     * 3. get response from clova
     * 4. save message entity with dId, userMessage, clovaMessage
     * 5. 부적은 별도로 저장하고, message entity id 랑 연관관계 매핑
     */
    fun sendMessage(userMessage: UserMessage): String {
        val prompt = SystemMessage.Companion.Prompt()
        val chatCompletionRequest = ChatCompletionRequest(
            messages = listOf(
                SystemMessage(role = prompt.role, content = prompt.content),
                SystemMessage(role = "user", content = userMessage.message),
            )
        )
        val response = ApiResponse.fromJson(getChatCompletion(chatCompletionRequest))
        println("chatCompletion = $response")

        val computerMood = response.result.message.content
        val now = LocalDateTime.now()
        val messageEntity = Message(
            userMood = userMessage.message,
            clovaMood = computerMood,
            deviceId = userMessage.deviceId,
            createdAt = now,
        )

        messageRepository.save(messageEntity)
        return computerMood
    }


    fun getChatCompletion(request: ChatCompletionRequest): String {
        return clovaServiceClient.getChatCompletion(apiKey, apiGwKey, requestId, request)
    }
}