package com.meesam.domain.dto

import jakarta.validation.constraints.Positive
import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class DeleteProfilePictureRequest @OptIn(ExperimentalUuidApi::class) constructor(
    @field:UUID(message = "userId must be valid")
    val id: Uuid,
    val profilePicUrl: String? = null
)
