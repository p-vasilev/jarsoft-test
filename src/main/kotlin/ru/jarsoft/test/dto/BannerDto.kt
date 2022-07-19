package ru.jarsoft.test.dto

import java.io.Serializable

data class BannerDto (
    val id: Long,
    val name: String,
    val text: String,
    val price: Double,
    val categories: List<CategoryDto>
): Serializable