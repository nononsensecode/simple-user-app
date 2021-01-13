package com.nononsensecode.simple.domain.model

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class User(
    @Id
    val id: UUID? = null,

    val createdAt: LocalDateTime,

    val username: String,

    val email: String,

    val firstName: String,

    val lastName: String,

    val isEnabled: Boolean,

    val isEmailVerified: Boolean,

    val password: String
)