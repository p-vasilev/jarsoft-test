package ru.jarsoft.test.dto.mapper

import org.springframework.stereotype.Component
import ru.jarsoft.test.dto.BannerDto
import ru.jarsoft.test.entity.Banner

@Component
class BannerDtoMapper(
    val categoryDtoMapper: CategoryDtoMapper
): DtoMapper<Banner, BannerDto> {

    // TODO might add banner validity check
    // assuming all banner DTOs are valid
    // (invalid ones should not be outside of db)
    override fun toDTO(entity: Banner) =
        BannerDto(
            entity.id,
            entity.name,
            entity.text,
            entity.price,
            entity.categories.map { categoryDtoMapper.toDTO(it) }
        )


    override fun fromDTO(dto: BannerDto) =
        Banner(
            dto.id,
            dto.name,
            dto.text,
            dto.price,
            dto.categories.map { categoryDtoMapper.fromDTO(it) },
            valid = true
        )
}