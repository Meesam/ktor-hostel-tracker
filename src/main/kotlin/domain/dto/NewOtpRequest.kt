package com.meesam.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Positive
import kotlinx.serialization.Serializable

@Serializable
data class NewOtpRequest(
    @field:NotBlank(message = "Phone number cannot be blank")
    val phoneNumber: String
)
