package ru.jarsoft.test.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.*

/**
 * DTO representing Banner entity
 * @param id id of the banner
 * @param name name of the banner
 * @param text the text that the banner contains
 * @param price price of the banner
 * @param categories categories that the banner relates to
 */
@Serializable
data class BannerDto (

    @Schema(example = "42", description = "id of the banner")
    val id: Long,

    @Schema(example = "Foo", description = "name of the banner")
    val name: String,

    @Schema(example = "Lorem Ipsum", description = "the text that the banner contains")
    val text: String,

    @Schema(example = "9.99", description = "price of the banner")
    val price: Double,

    @Schema(example = "[\"Music\", \"IT\"]", description = "categories that the banner relates to")
    val categories: List<CategoryDto>
)