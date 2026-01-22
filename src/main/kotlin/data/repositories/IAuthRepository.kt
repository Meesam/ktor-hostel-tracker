package com.meesam.data.repositories

import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.AuthenticationResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserResponse
import com.meesam.domain.dto.UserUpdateRequest
import kotlinx.serialization.Contextual
import java.util.UUID

interface IAuthRepository {
    suspend fun generateOtp(authenticationRequest: AuthenticationRequest): Int
    suspend fun register(userRequest: UserRequest): UserResponse
    suspend fun validateOtpAndLogin(otp: Int): UserResponse
    suspend fun getUserByPhoneNumber(phoneNumber: String): UserResponse
    suspend fun deleteAllOtpWithPhoneNumber(phoneNumber: String): Unit
    suspend fun saveRefreshToken(token: String, phoneNumber: String, userId: @Contextual UUID): Unit
    suspend fun updateUser(userUpdateRequest: UserUpdateRequest): Unit
}