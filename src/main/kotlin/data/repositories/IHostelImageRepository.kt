package com.meesam.data.repositories

import kotlinx.serialization.Contextual
import java.util.UUID

interface IHostelImageRepository {
    suspend fun addHostelImage(hostelId:@Contextual UUID, imagePath: String)
    suspend fun getAllHostelImages(hostelId:@Contextual UUID): List<String>
}