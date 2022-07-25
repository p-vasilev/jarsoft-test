package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.*
import ru.jarsoft.test.NotFoundException
import ru.jarsoft.test.dto.BannerWithoutId
import ru.jarsoft.test.dto.IdName
import ru.jarsoft.test.dto.BannerDto
import ru.jarsoft.test.dto.mapper.BannerDtoMapper
import ru.jarsoft.test.service.BannerService

@RestController
@RequestMapping("/banner")
class BannerController(
    val service: BannerService,
    val bannerDtoMapper: BannerDtoMapper
) {

    @GetMapping("/ids_and_names")
    fun getIdsAndNames(): List<IdName> {
        return service.getIdsAndNames()
    }

    @PostMapping("/new")
    fun createBanner(
        @RequestBody newBanner: BannerWithoutId
    ): Long {
        return service.createBanner(newBanner)
    }

    @GetMapping("/{id}")
    fun getBanner(
        @PathVariable id: Long
    ): BannerDto {
        val answer = service.getBannerById(id)
        if (answer.isEmpty)
            throw NotFoundException()
        return bannerDtoMapper.toDTO(answer.get())
    }

    @DeleteMapping("/{id}")
    fun deleteBanner(
        @PathVariable id: Long
    ) {
        service.deleteBannerById(id)
    }

    @PutMapping("/{id}")
    fun updateBanner(
        @PathVariable id: Long,
        @RequestBody newBanner: BannerWithoutId
    ) {
        service.updateBanner(id, newBanner)
    }

    @GetMapping("/all")
    fun getAllBanners(): List<BannerDto> {
        val serviceResult = service.getAllBanners()
        return serviceResult.map {
            bannerDtoMapper.toDTO(it)
        }
    }
}
