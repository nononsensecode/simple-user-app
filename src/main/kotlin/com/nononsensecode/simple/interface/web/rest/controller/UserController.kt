package com.nononsensecode.simple.`interface`.web.rest.controller

import at.favre.lib.crypto.bcrypt.BCrypt
import com.nononsensecode.simple.`interface`.web.dto.PasswordDTO
import com.nononsensecode.simple.`interface`.web.dto.UserDTO
import com.nononsensecode.simple.`interface`.web.exception.UserNotFoundException
import com.nononsensecode.simple.domain.model.IUserRepository
import com.nononsensecode.simple.spring.data.commons.OffsetBasedPageRequest
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.util.*

private val logger = KotlinLogging.logger {  }

@RestController
@RequestMapping("/api/v1.0/users")
class UserController(
    val userRepository: IUserRepository,

    @Value("\${application.timezone}")
    val timezone: String
) {

    private val zoneId: ZoneId = ZoneId.of(timezone)

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserDTO>> {
        logger.info { "Request came for finding all users" }
        val users = userRepository.findAll().map { UserDTO(it, zoneId) }
        return ResponseEntity(users, HttpStatus.OK)
    }

    @GetMapping("/id/{userId}")
    fun getUserById(@PathVariable("userId") userId: UUID): ResponseEntity<UserDTO> {
        logger.info { "Request came for finding users with id $userId" }
        val user = userRepository.findUserById(userId) ?: throw UserNotFoundException("User with id $userId not found")
        return ResponseEntity(UserDTO(user, zoneId), HttpStatus.OK)
    }

    @GetMapping("/username/{username}")
    fun getUserByUsername(@PathVariable("username") username: String): ResponseEntity<UserDTO> {
        logger.info { "Request came for finding user with username $username" }
        val user = userRepository.findUserByUsername(username) ?: throw UserNotFoundException("User with username $username not found")
        return ResponseEntity(UserDTO(user, zoneId), HttpStatus.OK)
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable("email") email: String): ResponseEntity<UserDTO> {
        logger.info { "Request came for finding user with email $email" }
        val user = userRepository.findUserByEmail(email) ?: throw UserNotFoundException("User with email $email is not found")
        return ResponseEntity(UserDTO(user, zoneId), HttpStatus.OK)
    }

    @PostMapping("/username/{username}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun validateCredentials(@RequestBody password: PasswordDTO, @PathVariable("username") username: String): ResponseEntity<Boolean> {
        val user = userRepository.findUserByUsername(username) ?: throw UserNotFoundException("User with username $username not found")
        val verified = BCrypt.verifyer().verify(password.password.toCharArray(), user.password).verified
        return ResponseEntity(verified, HttpStatus.OK)
    }

    @GetMapping("/count")
    fun countUsers(): ResponseEntity<Int> {
        logger.info { "Request for count of users" }
        val count = userRepository.getUsersCount()
        return ResponseEntity(count, HttpStatus.OK)
    }

    @GetMapping("/paged")
    fun getsPagedUsers(@RequestParam("start") start: Int,
                       @RequestParam("maxResults") maxResults: Int): ResponseEntity<List<UserDTO>> {
        logger.info { "Get users request in a pageable manner" }
        val pageable = OffsetBasedPageRequest(start, maxResults)
        val users = userRepository.getUsers(pageable)
        return ResponseEntity(users.content.map { UserDTO(it, zoneId) }, HttpStatus.OK)
    }

    @GetMapping("/search/{searchString}/paged")
    fun getPagedUsersBySearchString(@PathVariable("searchString") searchString: String,
                 @RequestParam("start") start: Int,
                 @RequestParam("maxResults") maxResults: Int): ResponseEntity<List<UserDTO>> {
        logger.info { "Search request with search string $searchString, starting position: $start, maximum results: $maxResults" }
        val pageable = OffsetBasedPageRequest(start, maxResults)
        val users = userRepository.findPagedUsersBySearchString(searchString, pageable)
        return ResponseEntity(users.content.map { UserDTO(it, zoneId) }, HttpStatus.OK)
    }

    @GetMapping("/search/{searchString}")
    fun getUsersBySearchString(@PathVariable("searchString") searchString: String): ResponseEntity<List<UserDTO>> {
        logger.info { "Search request with search string $searchString" }
        val users = userRepository.findUsersBySearchString(searchString)
        return ResponseEntity(users.map { UserDTO(it, zoneId) }, HttpStatus.OK)
    }
}