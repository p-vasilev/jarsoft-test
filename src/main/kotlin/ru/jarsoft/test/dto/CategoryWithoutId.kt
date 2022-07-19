package ru.jarsoft.test.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

/**
 * [CategoryDTO][ru.jarsoft.test.dto.CategoryDto] but without id
 * @param name name of the category
 * @param requestId string to use in requests to refer to this category
 */
@Serializable
data class CategoryWithoutId(

    @Schema(example = "Post Punk", description = "")
    val name: String,

    @Schema(example = "post_punk", description = "")
    val requestId: String
)
