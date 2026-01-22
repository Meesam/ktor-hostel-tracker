package com.meesam.services

import com.meesam.data.repositories.IAuthRepository
import com.meesam.domain.dto.AuthenticationRequest
import com.meesam.domain.dto.AuthenticationResponse
import com.meesam.domain.dto.UserRequest
import com.meesam.domain.dto.UserResponse
import com.meesam.domain.dto.UserUpdateRequest
import kotlinx.serialization.Contextual
import java.util.UUID

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

    override suspend fun saveRefreshToken(
        token: String,
        phoneNumber: String,
        userId: @Contextual UUID
    ) {
        return authRepository.saveRefreshToken(token, phoneNumber, userId)
    }

    override suspend fun updateUser(userUpdateRequest: UserUpdateRequest) {
        return authRepository.updateUser(userUpdateRequest)
    }
}