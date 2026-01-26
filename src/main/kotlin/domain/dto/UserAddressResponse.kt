package com.meesam.domain.dto


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class UserAddressResponse(
    val id: @Contextual UUID,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val address: String? = null,
    val country: String? = null,
    val userId: @Contextual UUID,
    val contactName: String? = null,
    val contactNumber: String? = null
)
