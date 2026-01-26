package com.meesam.services

import com.meesam.data.repositories.ICoachingRepository
import com.meesam.domain.dto.CoachingResponse
import com.meesam.domain.dto.NewCoachingRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateCoachingRequest
import kotlinx.serialization.Contextual
import java.util.UUID

class CoachingService(
    private val coachingRepository: ICoachingRepository
): ICoachingService {
    override suspend fun addNewCoaching(newCoachingRequest: NewCoachingRequest): CoachingResponse {
        return coachingRepository.addNewCoaching(newCoachingRequest)
    }

    override suspend fun delectCoaching(coachingId: @Contextual UUID) {
        return coachingRepository.delectCoaching(coachingId = coachingId)
    }

    override suspend fun updateCoaching(updateCoachingRequest: UpdateCoachingRequest): CoachingResponse {
        return coachingRepository.updateCoaching(updateCoachingRequest)
    }

    override suspend fun getCoachingById(coachingId: @Contextual UUID): CoachingResponse {
        return coachingRepository.getCoachingById(coachingId)
    }

    override suspend fun getAllCoaching(
        page: Int,
        size: Int
    ): PagedResponse<CoachingResponse> {
       return coachingRepository.getAllCoaching(page = page, size = size)
    }
}