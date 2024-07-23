package com.poten.clova.repository

import com.poten.clova.entity.Message
import com.poten.clova.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: JpaRepository<Message, Long>  {
}

