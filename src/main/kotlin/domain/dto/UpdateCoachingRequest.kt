package com.meesam.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UpdateCoachingRequest(
    @field:NotNull(message = "CoachingId cannot be blank")
    val coachingId:@Contextual  UUID,

    @field:NotBlank(message = "Name cannot be blank")
    val name: String,

    @field:NotBlank(message = "Address cannot be blank")
    val address: String,

    @field:NotBlank(message = "City cannot be blank")
    val city: String,

    @field:NotBlank(message = "State cannot be blank")
    val state: String,

    @field:NotBlank(message = "Country cannot be blank")
    val country: String,

    @field:NotBlank(message = "Zip code cannot be blank")
    val zipCode: String,

    @field:NotBlank(message = "Contact Name cannot be blank")
    val contactName: String,

    @field:NotBlank(message = "Contact number cannot be blank")
    val contactNumber: String
)
