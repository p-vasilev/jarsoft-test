package ru.jarsoft.test.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.jarsoft.test.ConflictException
import ru.jarsoft.test.NotFoundException
import ru.jarsoft.test.entity.Category
import ru.jarsoft.test.repository.BannerRepository
import ru.jarsoft.test.repository.CategoryRepository

@Service
class CategoryService(
    val repository: CategoryRepository,
    val bannerRepository: BannerRepository
) {
    @Transactional
    fun getAllCategories(): List<Category> {
        return repository.findAll().filter{ it.valid }
    }

    @Transactional
    fun createCategory(name: String, requestId: String): Long {
        val conflicts = repository.findAllByNameOrRequestId(name, requestId)
        if (conflicts.isNotEmpty())
            throw ConflictException()

        return repository.save(
            Category(
                0,
                name,
                requestId,
                valid = true
            )
        ).id
    }

    @Transactional
    fun updateCategory(id: Long, name: String, requestId: String) {
        val conflicts = repository.findAllByNameOrRequestId(name, requestId)
        if (conflicts.isNotEmpty() && !(conflicts.size == 1 && conflicts[0].id == id))
            throw ConflictException()

        repository.save(
            Category(
                id,
                name,
                requestId,
                valid = true
            )
        )
    }

    @Transactional
    fun deleteCategoryById(id: Long) {
        val banners = bannerRepository.findByCategory(id)
        if (banners.isNotEmpty() && banners.any { it.valid })
            throw ConflictException()
        val cat = repository.findById(id)
        if (cat.isEmpty)
            throw NotFoundException()
        else if (!cat.get().valid)
            throw NotFoundException()

        repository.deleteById(id)
    }

    fun getCategoryById(id: Long): Category {
        val res = repository.findById(id)
        if (res.isEmpty || !res.get().valid)
            throw NotFoundException()
        return res.get()
    }

}
