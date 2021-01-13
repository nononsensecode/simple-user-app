package com.nononsensecode.simple.`interface`.web.dto

import org.springframework.http.HttpStatus

data class ErrorDTO(
    val status: Int,
    val message: String
) {
    constructor(httpStatus: HttpStatus, message: String): this(httpStatus.value(), message)
}
