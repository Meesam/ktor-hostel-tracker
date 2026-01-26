package com.meesam.domain.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class UserAddressUpdateRequest @OptIn(ExperimentalUuidApi::class) constructor(

    @field:NotNull(message = "AddressId cannot be blank")
    val addressId:@Contextual java.util.UUID,

    @field:NotNull(message = "UserId cannot be blank")
    val userId:@Contextual java.util.UUID,

    @field:NotBlank(message = "Address cannot be blank")
    val address: String,

    @field:NotBlank(message = "City cannot be blank")
    val city: String,

    @field:NotBlank(message = "State cannot be blank")
    val state: String,

    @field:NotBlank(message = "Country cannot be blank")
    val country: String,

    @field:NotBlank(message = "ZipCode cannot be blank")
    val zipCode: String,

    @field:NotBlank(message = "Name cannot be blank")
    @field:Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters")
    val contactName: String = "",

    @field:NotBlank(message = "Contact number cannot be blank")
    @field:Size(min = 10, max = 15, message = "Contact number should be 10 digits")
    val contactNumber: String = "",
)
