package ru.jarsoft.test.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

/**
 * [BannerDTO][ru.jarsoft.test.dto.BannerDto] but without id
 * @param name name of the banner
 * @param text the text that the banner contains
 * @param price price of the banner
 * @param categories categories that the banner relates to
 */
@Serializable
data class BannerWithoutId(

    @Schema(example = "Cool banner", description = "name of the banner")
    val name: String,

    @Schema(example = "Lorem Ipsum", description = "the text that the banner contains")
    val text: String,

    @Schema(example = "9.99", description = "price of the banner")
    val price: Double,

    @Schema(example = "[\"Music\", \"IT\"]", description = "categories that the banner relates to")
    val categories: List<Long>
)
