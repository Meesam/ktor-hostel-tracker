package com.meesam.domain.dto

import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class HostelResponse(
    val id :@Contextual UUID,
    val name: String,
    val address: String,
    val city: String?,
    val state: String?,
    val country: String?,
    val zipCode: String?,
    val contactName: String,
    val contactNumber: String,
    val isActive: Boolean
)
