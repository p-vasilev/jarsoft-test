package ru.jarsoft.test.dto.mapper

import org.springframework.stereotype.Component
import ru.jarsoft.test.dto.CategoryDto
import ru.jarsoft.test.entity.Category

@Component
class CategoryDtoMapper: DtoMapper<Category, CategoryDto> {

    override fun toDTO(entity: Category) =
        CategoryDto(
            entity.id,
            entity.name,
            entity.requestId
        )

    override fun fromDTO(dto: CategoryDto) =
        Category(
            dto.id,
            dto.name,
            dto.requestId,
            valid = true
        )

}