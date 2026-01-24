package com.meesam.services

import com.meesam.data.repositories.IHostelPropertiesRepository
import com.meesam.domain.dto.HostelPropertyResponse
import com.meesam.domain.dto.NewHostelPropertyRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateHostelPropertyRequest
import kotlinx.serialization.Contextual
import java.util.UUID

class HostelPropertyService(
    private val hostelPropertiesRepository: IHostelPropertiesRepository
): IHostelPropertyService {
    override suspend fun addNewProperty(newHostelPropertyRequest: NewHostelPropertyRequest): HostelPropertyResponse {
       return hostelPropertiesRepository.addNewProperty(newHostelPropertyRequest)
    }

    override suspend fun deleteProperty(propertyId: @Contextual UUID) {
        return hostelPropertiesRepository.deleteProperty(propertyId)
    }

    override suspend fun getHostelProperty(propertyId: @Contextual UUID): HostelPropertyResponse {
        return hostelPropertiesRepository.getHostelProperty(propertyId = propertyId)
    }

    override suspend fun updateHostelProperty(updateHostelPropertyRequest: UpdateHostelPropertyRequest): HostelPropertyResponse {
        return hostelPropertiesRepository.updateHostelProperty(updateHostelPropertyRequest)
    }

    override suspend fun getAllHostelProperty(
        page: Int,
        size: Int,
        hostelId: @Contextual UUID
    ): PagedResponse<HostelPropertyResponse> {
       return hostelPropertiesRepository.getAllHostelProperty(page = page,size = size, hostelId = hostelId)
    }
}