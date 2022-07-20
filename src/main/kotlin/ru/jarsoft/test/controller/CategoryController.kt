package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.RestController
import ru.jarsoft.test.service.CategoryService

@RestController("/category")
class CategoryController(
    service: CategoryService
) {

}
