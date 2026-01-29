package com.meesam.services


import com.meesam.data.repositories.IHostelImageRepository
import kotlinx.serialization.Contextual
import java.util.UUID

class HostelImageService(
    private val hostelImageRepository: IHostelImageRepository
): IHostelImageService {
    override suspend fun addHostelImage(
        hostelId: @Contextual UUID,
        imagePath: String
    ) {
        return hostelImageRepository.addHostelImage(hostelId = hostelId, imagePath = imagePath)
    }

    override suspend fun getAllHostelImages(hostelId: @Contextual UUID): List<String> {
        return hostelImageRepository.getAllHostelImages(hostelId)
    }
}