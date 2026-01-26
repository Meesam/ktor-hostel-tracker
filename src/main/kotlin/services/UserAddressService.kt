package com.meesam.services

import com.meesam.data.repositories.IUserAddressRepository
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UserAddressRequest
import com.meesam.domain.dto.UserAddressResponse
import com.meesam.domain.dto.UserAddressUpdateRequest
import kotlinx.serialization.Contextual
import java.util.UUID

class UserAddressService(
    private val userAddressRepository: IUserAddressRepository
): IUserAddressService {
    override suspend fun addNewAddress(userAddressRequest: UserAddressRequest): UserAddressResponse {
        return userAddressRepository.addNewAddress(userAddressRequest)
    }

    override suspend fun updateAddress(updateRequest: UserAddressUpdateRequest): UserAddressResponse {
        return userAddressRepository.updateAddress(updateRequest)
    }

    override suspend fun deleteAddress(addressId: @Contextual UUID) {
        return userAddressRepository.deleteAddress(addressId)
    }

    override suspend fun getAddressById(addressId: @Contextual UUID): UserAddressResponse {
       return  userAddressRepository.getAddressById(addressId)
    }

    override suspend fun getAllAddressByUser(
        userId: @Contextual UUID,
        page: Int,
        size: Int
    ): PagedResponse<UserAddressResponse> {
        return  userAddressRepository.getAllAddressByUser(userId = userId, page = page, size = size)
    }
}