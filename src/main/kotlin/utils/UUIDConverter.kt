package com.meesam.utils

import java.util.UUID

fun String?.toUUIDOrNull(): UUID? = try {
    this?.let { UUID.fromString(it) }
} catch (e: IllegalArgumentException) {
    null
}