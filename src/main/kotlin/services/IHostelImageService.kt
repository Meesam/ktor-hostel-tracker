package com.meesam.services

import kotlinx.serialization.Contextual
import java.util.UUID

interface IHostelImageService {
    suspend fun addHostelImage(hostelId:@Contextual UUID, imagePath: String)
    suspend fun getAllHostelImages(hostelId:@Contextual UUID): List<String>
}