package com.nononsensecode.simple.utils

import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.toEpochMilli(zoneId: ZoneId): Long {
    val zoneDateTime = this.atZone(zoneId)
    return zoneDateTime.toInstant().toEpochMilli()
}