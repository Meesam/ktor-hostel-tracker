package com.meesam.services

import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.AuthenticationResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserResponse
import com.meesam.domain.dto.UserUpdateRequest
import kotlinx.serialization.Contextual
import java.util.UUID

interface IAuthService {
    suspend fun register(userRequest: UserRequest): UserResponse
    suspend fun validateOtpAndLogin(otp: Int): UserResponse
    suspend fun generateOtp(authenticationRequest: AuthenticationRequest): Int
    suspend fun saveRefreshToken(token: String, phoneNumber: String, userId: @Contextual UUID): Unit
    suspend fun updateUser(userUpdateRequest: UserUpdateRequest): Unit
}