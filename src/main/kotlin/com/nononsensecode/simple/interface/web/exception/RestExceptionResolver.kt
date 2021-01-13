package com.nononsensecode.simple.`interface`.web.exception

import com.nononsensecode.simple.`interface`.web.dto.ErrorDTO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class RestExceptionResolver: ResponseEntityExceptionHandler() {

    @ExceptionHandler(UserNotFoundException::class)
    fun userNotFoundException(e: UserNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val error = ErrorDTO(HttpStatus.NOT_FOUND, e.message)
        return handleExceptionInternal(e, error, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }
}