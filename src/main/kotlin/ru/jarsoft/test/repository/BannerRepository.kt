package ru.jarsoft.test.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.jarsoft.test.dto.IdName
import ru.jarsoft.test.entity.Banner
import java.util.*

@Repository
interface BannerRepository: CrudRepository<Banner, Long> {
    @Transactional
    @Query(value = "SELECT new ru.jarsoft.test.dto.IdName(b.id, b.name) FROM Banner b WHERE b.valid IS TRUE")
    fun getIdsAndNames(): List<IdName>

    // TODO JOIN FETCH here (and other queries) is not clean; should investigate a better solution to force
    //  hibernate to initialize categories (so that the fetching may stay lazy)
    @Transactional
    @Query(value = "SELECT b FROM Banner AS b LEFT JOIN FETCH b.categories WHERE b.id = :id AND b.valid = TRUE")
    override fun findById(id: Long): Optional<Banner>

    @Transactional
    @Query(value = "SELECT b FROM Banner AS b LEFT JOIN FETCH b.categories WHERE b.name = :name AND b.valid = TRUE")
    fun findByName(name: String): Optional<Banner>

    @Transactional
    @Modifying
    @Query(value = "UPDATE Banner b SET b.valid=FALSE WHERE b.id = :id")
    override fun deleteById(id: Long)

    @Transactional
    @Query(value = "SELECT b FROM Banner b, Category c LEFT JOIN FETCH b.categories WHERE b.valid IS TRUE AND c.id = :id AND c MEMBER OF b.categories")
    fun findByCategoryId(id: Long): List<Banner>

    @Transactional
    @Query(value = "SELECT DISTINCT b FROM Banner b LEFT JOIN FETCH b.categories WHERE b.valid = TRUE")
    fun findAllValid(): List<Banner>

    @Transactional
    @Query(value = "SELECT b FROM Banner b, Category c LEFT JOIN FETCH b.categories WHERE b.valid IS TRUE AND c MEMBER OF b.categories AND c.requestId IN (:categories)")
    fun findAllByCategoryRequestIds(categories: List<String>): List<Banner>

    @Transactional
    @Query(value = "SELECT DISTINCT b FROM Banner b, Category c LEFT JOIN FETCH b.categories WHERE b.valid IS TRUE AND c MEMBER OF b.categories AND c.requestId IN (:categories)")
    fun findByCategoryRequestIds(categories: List<String>): List<Banner>
}
