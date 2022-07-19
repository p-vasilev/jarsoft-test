package ru.jarsoft.test.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

/**
 * DTO for Category entity
 * @param id id of the category
 * @param name name of the category
 * @param requestId string to use in requests to refer to this category
 */
@Serializable
data class CategoryDto (

    @Schema(example = "42", description = "id of the category")
    val id: Long,

    @Schema(example = "Music", description = "name of the category")
    val name: String,

    @Schema(example = "music", description = "string to use in requests to refer to this category")
    val requestId: String
)
