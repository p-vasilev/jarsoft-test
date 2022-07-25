package ru.jarsoft.test

import org.springframework.stereotype.Component
import ru.jarsoft.test.repository.BannerRepository
import ru.jarsoft.test.repository.CategoryRepository
import ru.jarsoft.test.repository.LogRepository
import ru.jarsoft.test.repository.UserAgentRepository

/**
 * -----IMPORTANT-----
 * ONLY TO BE USED FOR TESTS
 * This class is used to DELETE ALL RECORDS FROM THE DATABASE
 * (to rollback changes during tests)
 */
@Component
class DatabaseCleaner(
    val bannerRepository: BannerRepository,
    val categoryRepository: CategoryRepository,
    val logRepository: LogRepository,
    val userAgentRepository: UserAgentRepository
) {
    /**
    * -----IMPORTANT-----
    * ONLY TO BE USED FOR TESTS
    * Deletes ALL RECORDS from the database
    */
    fun deleteEverything() {
        logRepository.deleteAll()
        userAgentRepository.deleteAll()
        bannerRepository.deleteAll()
        categoryRepository.deleteAll()
    }

}
