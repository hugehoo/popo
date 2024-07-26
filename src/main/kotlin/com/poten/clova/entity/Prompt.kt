package com.poten.clova.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "prompt")
data class Prompt (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "role", nullable = false)
    val role: String,

    @Column(name = "content", nullable = false)
    val content: String,

    @Column(name = "version", nullable = false)
    val version: String,

    @Column(name = "latest_version", nullable = false)
    val latestVersion: Boolean,

    @Column(name = "system_type", nullable = false)
    val systemType: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = true)
    val updatedAt: LocalDateTime? = null

)