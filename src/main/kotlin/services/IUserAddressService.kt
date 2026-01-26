package com.meesam.services

import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UserAddressRequest
import com.meesam.domain.dto.UserAddressResponse
import com.meesam.domain.dto.UserAddressUpdateRequest
import kotlinx.serialization.Contextual
import java.util.UUID

interface IUserAddressService {
    suspend fun addNewAddress(userAddressRequest: UserAddressRequest): UserAddressResponse

    suspend fun updateAddress(updateRequest: UserAddressUpdateRequest): UserAddressResponse

    suspend fun deleteAddress(addressId: @Contextual UUID): Unit

    suspend fun getAddressById(addressId: @Contextual UUID): UserAddressResponse

    suspend fun getAllAddressByUser(userId: @Contextual UUID,page: Int, size: Int):PagedResponse<UserAddressResponse>
}