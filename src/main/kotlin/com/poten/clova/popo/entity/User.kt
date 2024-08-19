package com.poten.clova.popo.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "users")
data class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    val age: String,

    @Column(nullable = false)
    val deviceId: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false)
    val updatedAt: LocalDateTime

)