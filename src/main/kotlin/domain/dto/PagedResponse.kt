package com.meesam.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
    val data: List<T>,
    val currentPage: Int,
    val totalPages: Long,
    val totalItems: Long
)
