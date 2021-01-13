package com.nononsensecode.simple.infrastructure

class DataAccessException(
    override val message: String,
    override val cause: Throwable
): RuntimeException(message, cause)