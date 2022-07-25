package ru.jarsoft.test.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    @Transactional
    fun getIdsAndNames(): List<IdName> {
        return repository.getIdsAndNames()
    }

    @Transactional
    fun createBanner(banner: BannerWithoutId): Long {
        val result = repository.findByName(banner.name)
        if (result.isPresent)
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

    @Transactional
    fun getBannerById(id: Long): Optional<Banner> {
        return repository.findById(id)
    }

    @Transactional
    fun deleteBannerById(id: Long) {
        val banOpt = repository.findById(id)
        if (banOpt.isEmpty)
            throw NotFoundException()
        repository.deleteById(id)
    }

    @Transactional
    fun updateBanner(id: Long, banner: BannerWithoutId) {
        val res = repository.findByName(banner.name)
        if (res.isPresent && res.get().id != id)
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

    @Transactional
    fun getAllBanners(): List<Banner> {
        return repository.findAllValid()
    }

}
