package com.meesam.data.repositories

import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.AuthenticationResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserResponse

interface IAuthRepository {
    suspend fun generateOtp(authenticationRequest: AuthenticationRequest): Int
    suspend fun register(userRequest: UserRequest): UserResponse
    suspend fun validateOtpAndLogin(otp: Int): UserResponse
    suspend fun getUserByPhoneNumber(phoneNumber: String): UserResponse
    suspend fun deleteAllOtpWithPhoneNumber(phoneNumber: String): Unit
}