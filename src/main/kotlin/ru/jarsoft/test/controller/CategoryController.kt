package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.*
import ru.jarsoft.test.service.CategoryService

@RestController("/category")
class CategoryController(
    service: CategoryService
) {

    @GetMapping("/all")
    fun getAllCategories() {
        TODO()
    }

    @PostMapping("/new")
    fun createCategory(
        @RequestParam name: String,
        @RequestParam requestId: String
    ) {
        TODO()
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam requestId: String
    ) {
        TODO()
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(
        @PathVariable id: Long
    ) {
        TODO()
    }
}
