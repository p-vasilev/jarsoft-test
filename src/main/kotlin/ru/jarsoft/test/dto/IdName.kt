package ru.jarsoft.test.dto

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

/**
 * DTO holding an id and a name (used to transfer ids and names of categories/banners)
 * @param id id of the referenced object
 * @param name name of the referenced object
 */
@Serializable
data class IdName(

    @Schema(example = "42", description = "id of the referenced object")
    val id: Long,

    @Schema(example = "Thingy", description = "name of the referenced object")
    val name: String
)
