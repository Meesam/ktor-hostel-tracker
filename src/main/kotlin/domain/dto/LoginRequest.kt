package com.meesam.domain.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @Min(value = 100000, message = "Otp Must be at least 6 digits")
    @Max(value = 999999, message = "Otp Must not exceed 6 digits")
    val otp: Int
)
