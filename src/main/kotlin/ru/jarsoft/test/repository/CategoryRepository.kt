package ru.jarsoft.test.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.jarsoft.test.entity.Category

@Repository
interface CategoryRepository: CrudRepository<Category, Long> {
    @Query(value = "SELECT c FROM Category c WHERE c.name = :name OR c.requestId = :requestId")
    fun findAllByNameOrRequestId(name: String, requestId: String): List<Category>

}
