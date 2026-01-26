package com.meesam.data.repositories

import com.meesam.domain.dto.CoachingResponse
import com.meesam.domain.dto.NewCoachingRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateCoachingRequest
import kotlinx.serialization.Contextual
import java.util.UUID

interface ICoachingRepository {
    suspend fun addNewCoaching(newCoachingRequest: NewCoachingRequest): CoachingResponse
    suspend fun delectCoaching(coachingId:@Contextual UUID): Unit
    suspend fun updateCoaching(updateCoachingRequest: UpdateCoachingRequest): CoachingResponse
    suspend fun getCoachingById(coachingId: @Contextual UUID): CoachingResponse
    suspend fun getAllCoaching(page: Int, size: Int): PagedResponse<CoachingResponse>
}