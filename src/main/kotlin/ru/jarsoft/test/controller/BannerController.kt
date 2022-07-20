package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.jarsoft.test.dto.BannerWithoutId
import ru.jarsoft.test.service.BannerService

@RestController("/banner")
class BannerController(
    service: BannerService
) {
    @GetMapping("/ids_and_names")
    fun getIdsAndNames() {
        TODO()
    }

    @PostMapping("/new")
    fun createBanner(
        @RequestBody newBanner: BannerWithoutId
    ) {
        TODO()
    }

    @GetMapping("/{id}")
    fun getBanner(
        @PathVariable id: Long
    ) {
        TODO()
    }

    @DeleteMapping("/{id}")
    fun deleteBanner(
        @PathVariable id: Long
    ) {
        TODO()
    }

    @PutMapping
    fun updateBanner(
        @PathVariable id: Long,
        @RequestBody newBanner: BannerWithoutId
    ) {
        TODO()
    }
}
