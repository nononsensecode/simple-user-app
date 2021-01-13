package com.nononsensecode.simple.domain.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

interface IUserRepository {
    fun findUserByEmail(email: String): User?
    fun findUserByUsername(username: String): User?
    fun findUserById(id: UUID): User?
    fun findAll(): List<User>
    fun getUsersCount(): Int
    fun getUsers(pageable: Pageable): Page<User>
    fun findPagedUsersBySearchString(searchString: String, pageable: Pageable): Page<User>
    fun findUsersBySearchString(searchString: String): List<User>
    fun findUsersBySearchString(searchString: String, pageable: Pageable): List<User>
}