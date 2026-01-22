package com.meesam.domain.dto

import jakarta.validation.constraints.Positive
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import java.util.UUID


@Serializable
data class UserUpdateRequest @OptIn(ExperimentalUuidApi::class) constructor(
    val id: @Contextual UUID,
    val name: String? = null,
    val dob: kotlinx.datetime.LocalDate? = null,
    val profilePicUrl: String? = null,
    val phone: String? = null,
    val email: String? = null
)
