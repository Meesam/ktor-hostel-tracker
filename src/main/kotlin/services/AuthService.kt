package com.meesam.services

import com.meesam.data.repositories.IAuthRepository
import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.AuthenticationResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserResponse

class AuthService(
private val authRepository: IAuthRepository
): IAuthService {
    override suspend fun register(userRequest: UserRequest): UserResponse {
        return authRepository.register(userRequest)
    }
    override suspend fun validateOtpAndLogin(otp: Int): UserResponse {
        return authRepository.validateOtpAndLogin(otp)
    }

    override suspend fun generateOtp(authenticationRequest: AuthenticationRequest): Int {
        authRepository.deleteAllOtpWithPhoneNumber(authenticationRequest.phoneNumber)
        return authRepository.generateOtp(authenticationRequest)
    }
}