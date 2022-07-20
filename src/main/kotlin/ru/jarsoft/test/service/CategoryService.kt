package ru.jarsoft.test.service

import org.springframework.stereotype.Service
import ru.jarsoft.test.repository.CategoryRepository

@Service
class CategoryService(
    repository: CategoryRepository
) {

}
