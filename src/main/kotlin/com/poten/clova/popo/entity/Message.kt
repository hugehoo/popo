package com.poten.clova.popo.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "message")
data class Message (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_mood", nullable = false)
    val userMood: String,

    @Column(name = "clova_mood", nullable = false)
    val clovaMood: String,

//    @Column(name = "vicky_mood", nullable = true)
//    val vickyMood: String,

    @Column(name = "character", nullable = false)
    val character: String,

    @Column(nullable = false)
    val deviceId: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = true)
    val updatedAt: LocalDateTime? = null

)