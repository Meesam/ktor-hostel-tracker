package com.meesam.domain.dto

import jakarta.validation.constraints.Positive
import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Serializable
data class UserUpdateRequest @OptIn(ExperimentalUuidApi::class) constructor(
    @field:UUID(message = "userId must be valid")
    val id: Uuid ,
    val name: String? = null,
    val dob: kotlinx.datetime.LocalDate? = null,
    val profilePicUrl: String? = null,
    val phone: String? = null,
)
