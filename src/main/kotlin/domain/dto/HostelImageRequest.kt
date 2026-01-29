package com.meesam.domain.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class HostelImageRequest(
    @field:NotBlank(message = "imagePath cannot be blank")
    val imagePath: String,

    @field:NotNull(message = "Hostel Id cannot be null")
    val hostelId: @Contextual UUID,
)
