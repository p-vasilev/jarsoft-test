package ru.jarsoft.test.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.jarsoft.test.dto.IdName
import ru.jarsoft.test.entity.Banner
import java.util.*

@Repository
interface BannerRepository: CrudRepository<Banner, Long> {
    @Query(value = "SELECT id, name FROM Banner WHERE valid IS TRUE")
    fun getIdsAndNames(): List<IdName>

    @Query(value = "SELECT b FROM Banner AS b WHERE b.id = :id AND b.valid IS TRUE")
    override fun findById(id: Long): Optional<Banner>

    @Query(value = "SELECT b FROM Banner AS b WHERE b.name = :name AND b.valid IS TRUE")
    fun findByName(name: String): Result<Banner>

    @Query(value = "UPDATE Banner b SET b.valid=FALSE WHERE b.id = :id")
    override fun deleteById(id: Long)

    @Query(value = "SELECT b FROM Banner b, Category c WHERE b.valid IS TRUE AND c.id = :id AND c MEMBER OF b.categories")
    fun findByCategory(id: Long): List<Banner>

}
