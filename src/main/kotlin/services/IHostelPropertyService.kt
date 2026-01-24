package com.meesam.services

import com.meesam.domain.dto.HostelPropertyResponse
import com.meesam.domain.dto.NewHostelPropertyRequest
import com.meesam.domain.dto.PagedResponse
import com.meesam.domain.dto.UpdateHostelPropertyRequest
import kotlinx.serialization.Contextual
import java.util.UUID

interface IHostelPropertyService {
    suspend fun addNewProperty(newHostelPropertyRequest: NewHostelPropertyRequest): HostelPropertyResponse
    suspend fun deleteProperty(propertyId:@Contextual UUID)
    suspend fun getHostelProperty(propertyId:@Contextual UUID):HostelPropertyResponse
    suspend fun updateHostelProperty(updateHostelPropertyRequest: UpdateHostelPropertyRequest):HostelPropertyResponse
    suspend fun getAllHostelProperty(page: Int, size: Int, hostelId: @Contextual UUID): PagedResponse<HostelPropertyResponse>
}