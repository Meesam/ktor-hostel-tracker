package com.meesam.domain.dto

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@Serializable
data class UserResponse @OptIn(ExperimentalUuidApi::class) constructor(
    val id: @Contextual UUID,
    val name: String = "",
    val email: String? = null,
    val dob: LocalDate? = null,
    val lastLoginAt: LocalDateTime? = null,
    val role: String,
    val profilePicUrl: String? = null,
    val otp: Int? = null,
    val phoneNumber: String? = null,
)
