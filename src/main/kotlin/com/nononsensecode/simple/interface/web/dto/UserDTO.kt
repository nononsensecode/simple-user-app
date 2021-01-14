package com.nononsensecode.simple.`interface`.web.dto

import com.nononsensecode.simple.domain.model.User
import com.nononsensecode.simple.utils.toEpochMilli
import java.time.ZoneId
import java.util.*

data class UserDTO(
    val id: UUID,
    val createdAt: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val enabled: Boolean,
    val emailVerified: Boolean,
    val roles: Set<RoleDTO>
) {
    constructor(user: User, zoneId: ZoneId): this(
        id = user.id!!,
        createdAt = user.createdAt.toEpochMilli(zoneId),
        username = user.username,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        enabled = user.isEnabled,
        emailVerified = user.isEmailVerified,
        roles = user.roles.map { RoleDTO(it.id, it.name, it.client.name) }.toSet()
    )
}