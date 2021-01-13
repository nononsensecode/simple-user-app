package com.nononsensecode.simple.`interface`.web.exception

class UserNotFoundException(
    override val message: String
): RuntimeException(message)