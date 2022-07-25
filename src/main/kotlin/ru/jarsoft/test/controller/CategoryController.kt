package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.*
import ru.jarsoft.test.dto.CategoryDto
import ru.jarsoft.test.dto.mapper.CategoryDtoMapper
import ru.jarsoft.test.service.CategoryService

@RestController
@RequestMapping("/category")
class CategoryController(
    val service: CategoryService,
    val categoryDtoMapper: CategoryDtoMapper
) {

    @GetMapping("/all")
    fun getAllCategories(): List<CategoryDto> {
        return service.getAllCategories().map {
            categoryDtoMapper.toDTO(it)
        }
    }

    @PostMapping("/new")
    fun createCategory(
        @RequestParam name: String,
        @RequestParam requestId: String
    ): Long {
        return service.createCategory(name, requestId)
    }

    @GetMapping("/{id}")
    fun getCategory(
        @PathVariable id: Long
    ): CategoryDto {
        val res = service.getCategoryById(id)
        return categoryDtoMapper.toDTO(res)
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
