package com.meesam.domain.dto

import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

//import org.springframework.web.multipart.MultipartFile

@Serializable
data class UserProfilePictureRequest @OptIn(ExperimentalUuidApi::class) constructor(
    @field:UUID(message = "userId must be valid")
    val userId: Uuid
)
