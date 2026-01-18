package com.meesam.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(

    @field:NotBlank(message = "name cannot be blank")
    @field:Size(min = 3, max = 100, message = "name must be between 3 and 100 characters")
    val name :String = "",

    @field:Email(message = "invalid email address")
    val email: String? = null,

    val password: String? = null,

    val role:String? = null,

    val dob: Instant? = null,
    val lastLoginAt: Instant? = null,
    @field:NotBlank(message = "email cannot be blank")
    val phone: String = "",
)
