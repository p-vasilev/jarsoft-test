package ru.jarsoft.test.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.jarsoft.test.entity.Category

@Repository
interface CategoryRepository: CrudRepository<Category, Long> {
    @Transactional
    @Query(value = "SELECT c FROM Category c WHERE c.name = :name OR c.requestId = :requestId")
    fun findAllByNameOrRequestId(name: String, requestId: String): List<Category>

    @Transactional
    @Modifying
    @Query(value = "UPDATE Category c SET c.valid = FALSE WHERE c.id = :id")
    override fun deleteById(id: Long)

}
