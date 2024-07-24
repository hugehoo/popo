package com.poten.clova.service

import com.poten.clova.dto.Onboard
import com.poten.clova.entity.User
import com.poten.clova.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class UserService(private val userRepository: UserRepository) {

    fun getUserById(deviceId: String): User? {
        return userRepository.findByDeviceId(deviceId);
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