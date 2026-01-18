package com.meesam.domain.dto

import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRequest(
    @field:NotBlank(message = "Phone number cannot be blank")
    val phoneNumber: String,
)
