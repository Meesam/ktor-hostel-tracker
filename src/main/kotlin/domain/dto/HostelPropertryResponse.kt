package com.meesam.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class HostelPropertyResponse(
    val propertyId:@Contextual  UUID,
    val propertyName: String,
    val propertyValue: String,
    val hostelId:@Contextual  UUID,
    val isActive: Boolean
)
