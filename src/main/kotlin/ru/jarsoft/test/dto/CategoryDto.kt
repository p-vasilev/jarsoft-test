package ru.jarsoft.test.dto

import java.io.Serializable

data class CategoryDto (
    val id: Long,
    val name: String,
    val requestId: String
): Serializable
