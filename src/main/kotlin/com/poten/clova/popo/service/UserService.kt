package com.poten.clova.popo.service

import com.poten.clova.popo.dto.MessageDto
import com.poten.clova.popo.dto.Onboard
import com.poten.clova.popo.dto.OnboardDto
import com.poten.clova.popo.entity.User
import com.poten.clova.popo.enum.AgeCategory
import com.poten.clova.popo.repository.MessageRepository
import com.poten.clova.popo.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class UserService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) {

    fun getUserById(deviceId: String): Onboard {
        val user = userRepository.findByDeviceId(deviceId)
        return Onboard(
            name = user.name,
            age = AgeCategory.valueOf(user.age),
            deviceId = user.deviceId
        )
    }

    fun getMyPage(deviceId: String, pageable: Pageable): Page<MessageDto> {
        val messages = messageRepository.findAllByDeviceId(deviceId, pageable)
        return messages.map { MessageDto.by(it) }
    }

    fun saveUser(onboard: OnboardDto) {
        val now = LocalDateTime.now()
        val user = User(
            name = onboard.name,
            age = onboard.age!!.name,
            deviceId = onboard.deviceId,
            createdAt = now,
            updatedAt = now
        )
        userRepository.save(user)
    }

    @Transactional
    fun updateName(onboard: OnboardDto): String {
        val user = userRepository.findByDeviceId(onboard.deviceId)

        user.name = onboard.name
        userRepository.save(user)

        return "Success"
    }

}