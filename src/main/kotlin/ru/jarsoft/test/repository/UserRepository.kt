package ru.jarsoft.test.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.jarsoft.test.entity.User
import java.util.*

@Repository
interface UserRepository: CrudRepository<User, Long> {
    @Query(value = "SELECT u FROM User u WHERE u.name = :name")
    fun findByName(name: String): Optional<User>
}
