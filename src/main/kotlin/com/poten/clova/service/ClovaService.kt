package com.poten.clova.service

import com.poten.clova.dto.*
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
    fun sendMessage(userMessage: UserMessage): ClovaMessage {
        val prompt = SystemMessage.Companion.Prompt()
        val chatCompletionRequest = ChatCompletionRequest(
            messages = listOf(
                SystemMessage(role = prompt.role, content = prompt.content),
                SystemMessage(role = "user", content = userMessage.message),
            )
        )
        val vickyPrompt = SystemMessage.Companion.VickyPrompt()
        val vickyCompletionRequest = ChatCompletionRequest(
            messages = listOf(
                SystemMessage(role = vickyPrompt.role, content = vickyPrompt.content),
                SystemMessage(role = "user", content = userMessage.message),
            )
        )

        val computerMood = ApiResponse.fromJson(getChatCompletion(chatCompletionRequest))
            .result
            .message
            .content

        val vickyMood = ApiResponse.fromJson(getChatCompletion(vickyCompletionRequest))
            .result
            .message
            .content

        val now = LocalDateTime.now()
        val messageEntity = Message(
            userMood = userMessage.message,
            clovaMood = computerMood,
            vickyMood = vickyMood,
            deviceId = userMessage.deviceId,
            character = "popo",
            createdAt = now,
        )

        messageRepository.save(messageEntity)
        return ClovaMessage(clova_mood = computerMood, vicky_mood = vickyMood)
    }


    fun generateCharm(userMessage: UserMessage): CharmDto {

        val charmPrompt = SystemMessage.Companion.CharmPrompt()
        val chatCompletionRequest = ChatCompletionRequest(
            messages = listOf(
                SystemMessage(role = charmPrompt.role, content = charmPrompt.content),
                SystemMessage(role = "user", content = userMessage.message),
            )
        )

        val (_, result) = ApiResponse.fromJson(getChatCompletion(chatCompletionRequest))
        val (fourIdioms, message) = ApiResponse.Companion.CharmMessage.getCharmMessage(result.message.content)
        return CharmDto(four_idioms = fourIdioms, message = message)
    }

    fun getChatCompletion(request: ChatCompletionRequest): String {
        return clovaServiceClient.getChatCompletion(apiKey, apiGwKey, requestId, request)
    }
}