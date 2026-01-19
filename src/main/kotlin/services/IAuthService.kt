package com.meesam.services

import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.AuthenticationResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserResponse

interface IAuthService {
    suspend fun register(userRequest: UserRequest): UserResponse
    suspend fun validateOtpAndLogin(otp: Int): UserResponse
    suspend fun generateOtp(authenticationRequest: AuthenticationRequest): Int
}