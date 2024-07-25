package com.poten.clova.repository

import com.poten.clova.entity.Message
import com.poten.clova.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable

@Repository
interface MessageRepository: JpaRepository<Message, Long>  {

    fun findAllByDeviceId(deviceId: String, pageable: Pageable) : Page<Message>

}

