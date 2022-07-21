package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.*
import ru.jarsoft.test.entity.Category
import ru.jarsoft.test.service.CategoryService

@RestController("/category")
class CategoryController(
    val service: CategoryService
) {

    @GetMapping("/all")
    fun getAllCategories(): List<Category> {
        return service.getAllCategories()
    }

    @PostMapping("/new")
    fun createCategory(
        @RequestParam name: String,
        @RequestParam requestId: String
    ): Long {
        return service.createCategory(name, requestId)
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam requestId: String
    ) {
        service.updateCategory(id, name, requestId)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(
        @PathVariable id: Long
    ) {
        service.deleteCategoryById(id)
    }
}
