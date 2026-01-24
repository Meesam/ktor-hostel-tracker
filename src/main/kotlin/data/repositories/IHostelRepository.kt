package com.meesam.data.repositories

import com.meesam.domain.dto.HostelResponse
import com.meesam.domain.dto.NewHostelRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateHostelRequest
import kotlinx.serialization.Contextual
import java.util.UUID

interface IHostelRepository {
    suspend fun addNewHostel(newHostelRequest: NewHostelRequest): HostelResponse
    suspend fun delectHostel(hostelId:@Contextual UUID): Unit
    suspend fun updateHostel(updateHostelRequest: UpdateHostelRequest): HostelResponse
    suspend fun getHostelById(hostelId: @Contextual UUID): HostelResponse
    suspend fun getAllHostel(page: Int, size: Int): PagedResponse<HostelResponse>
}