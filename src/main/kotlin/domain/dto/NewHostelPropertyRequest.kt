package com.meesam.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NewHostelPropertyRequest(
    @field:NotBlank(message = "Property Name cannot be blank")
    val propertyName: String,

    @field:NotBlank(message = "Property Value cannot be blank")
    val propertyValue: String,

    @field:NotNull(message = "HostelId cannot be blank")
    val hostelId:@Contextual  UUID,

)
