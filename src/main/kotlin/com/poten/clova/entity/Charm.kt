package com.poten.clova.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "sentence")
data class Charm (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

//    @OneToOne
//    @Column(name = "sentence_id", nullable = false)
//    val sentence: Sentence,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "charm_type", nullable = false)
    val charmType: Int,

    @Column(name = "charm_message", nullable = false)
    val charmMessage: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false)
    val updatedAt: LocalDateTime

)