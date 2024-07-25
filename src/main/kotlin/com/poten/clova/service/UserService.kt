package com.poten.clova.service

import com.poten.clova.dto.MessageDto
import com.poten.clova.dto.Onboard
import com.poten.clova.entity.User
import com.poten.clova.repository.MessageRepository
import com.poten.clova.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime


@Service
class UserService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository) {

    fun getUserById(deviceId: String): User? {
        return userRepository.findByDeviceId(deviceId);
    }

    fun getMyPage(deviceId: String, pageable: Pageable): Page<MessageDto> {
        val messages = messageRepository.findAllByDeviceId(deviceId, pageable)
        return messages.map { it -> MessageDto.by(it) };
    }

    fun saveUser(onboard: Onboard) {
        val now = LocalDateTime.now()
        val user = User(
            name = onboard.name,
            age = onboard.age.name,
            deviceId = onboard.deviceId,
            createdAt = now,
            updatedAt = now
        )
        userRepository.save(user)
    }

}