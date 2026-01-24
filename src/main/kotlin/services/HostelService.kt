package com.meesam.services

import com.meesam.data.repositories.IHostelRepository
import com.meesam.domain.dto.HostelResponse
import com.meesam.domain.dto.NewHostelRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateHostelRequest
import kotlinx.serialization.Contextual
import java.util.UUID

class HostelService(
    private val hostelRepository: IHostelRepository
): IHostelService {
    override suspend fun addNewHostel(newHostelRequest: NewHostelRequest): HostelResponse {
        return hostelRepository.addNewHostel(newHostelRequest)
    }

    override suspend fun delectHostel(hostelId: @Contextual UUID) {
       return hostelRepository.delectHostel(hostelId)
    }

    override suspend fun updateHostel(updateHostelRequest: UpdateHostelRequest): HostelResponse {
        return hostelRepository.updateHostel(updateHostelRequest)
    }

    override suspend fun getHostelById(hostelId: @Contextual UUID): HostelResponse {
        return hostelRepository.getHostelById(hostelId)
    }

    override suspend fun getAllHostel(
        page: Int,
        size: Int
    ): PagedResponse<HostelResponse> {
        return hostelRepository.getAllHostel(page, size)
    }
}