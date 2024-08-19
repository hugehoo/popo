package com.poten.clova.popo.service

import com.poten.clova.dto.*
import com.poten.clova.popo.entity.Message
import com.poten.clova.popo.entity.Prompt
import com.poten.clova.popo.enum.Character
import com.poten.clova.popo.dto.*
import com.poten.clova.popo.repository.MessageRepository
import com.poten.clova.popo.repository.PromptRepository
import com.poten.clova.popo.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*


@Service
class ClovaService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val promptRepository: PromptRepository,
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
    @Transactional
    fun sendMessage(userMessage: UserMessage): ClovaMessage {
        return when (userMessage.character) {
            Character.POPO.name -> {
                val (_, role, content, _, _, _, _, _) = getPrompt(Character.POPO.name)
                processMessage(userMessage, SystemMessage.Companion.Prompt(role, content), Character.POPO)
            }
            else -> {
                val (_, role, content, _, _, _, _, _) = getPrompt(Character.VICKY.name)
                processMessage(userMessage, SystemMessage.Companion.Prompt(role, content), Character.VICKY)
            }
        }
    }

    private fun processMessage(userMessage: UserMessage,
                               prompt: SystemMessage.Companion.Prompt,
                               characterName: Character
    ): ClovaMessage {

        val chatCompletionRequest = ChatCompletionRequest(
            messages = listOf(
                SystemMessage(role = prompt.role, content = prompt.content),
                SystemMessage(role = "user", content = userMessage.message)
            )
        )

        val clovaMood = ApiResponse.fromJson(getChatCompletion(chatCompletionRequest))
            .result
            .message
            .content

        val messageEntity = Message(
            userMood = userMessage.message,
            clovaMood = clovaMood,
            deviceId = userMessage.deviceId,
            character = characterName.name,
            createdAt = LocalDateTime.now()
        )

        messageRepository.save(messageEntity)
        return ClovaMessage(clova_mood = clovaMood, character = characterName.name)
    }

    fun generateCharm(userMessage: UserMessage): CharmDto {

        val (_, role, content, _, _, _) = getPrompt(Character.CHARM.name)
        val chatCompletionRequest = ChatCompletionRequest(
            messages = listOf(
                SystemMessage(role = role, content = content),
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

    private fun getPrompt(character: String): Prompt {
        return promptRepository.findBySystemTypeAndLatestVersion(character, true)
    }
}