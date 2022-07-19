package ru.jarsoft.test.dto.mapper

/**
 * Describes a mapping between a model entity class and its corresponding DTO class
 */
interface DtoMapper<E, D> {
    /**
     * Converts an entity to a DTO
     */
    fun toDTO(entity: E): D

    /**
     * Converts a DTO to an entity
     */
    fun fromDTO(dto: D): E
}