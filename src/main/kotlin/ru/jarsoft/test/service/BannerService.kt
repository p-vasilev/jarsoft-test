package ru.jarsoft.test.service

import org.springframework.stereotype.Service
import ru.jarsoft.test.ConflictException
import ru.jarsoft.test.NotFoundException
import ru.jarsoft.test.dto.BannerWithoutId
import ru.jarsoft.test.dto.IdName
import ru.jarsoft.test.entity.Banner
import ru.jarsoft.test.repository.BannerRepository
import ru.jarsoft.test.repository.CategoryRepository
import java.util.*

@Service
class BannerService(
    val repository: BannerRepository,
    val categoryRepository: CategoryRepository
) {

    fun getIdsAndNames(): List<IdName> {
        return repository.getIdsAndNames()
    }

    fun createBanner(banner: BannerWithoutId): Long {
        val result = repository.findByName(banner.name)
        if (result.isSuccess)
            throw ConflictException()

        return repository.save(
            Banner(
                0,
                banner.name,
                banner.text,
                banner.price,
                categoryRepository.findAllById(banner.categories).toList(),
                valid = true
            )
        ).id
    }

    fun getBannerById(id: Long): Optional<Banner> {
        return repository.findById(id)
    }

    fun deleteBannerById(id: Long) {
        val banOpt = repository.findById(id)
        if (banOpt.isEmpty)
            throw NotFoundException()
        repository.deleteById(id)
    }

    fun updateBanner(id: Long, banner: BannerWithoutId) {
        val res = repository.findByName(banner.name)
        if (res.isSuccess && res.getOrNull()!!.id == id)
            throw ConflictException()

        repository.save(
            Banner(
                id,
                banner.name,
                banner.text,
                banner.price,
                categoryRepository.findAllById(banner.categories).toList(),
                valid = true
            )
        )
    }
}
