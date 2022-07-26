package ru.jarsoft.test.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.jarsoft.test.entity.UserAgent

@Repository
interface UserAgentRepository: CrudRepository<UserAgent, Long> {

    @Query(value = "SELECT ua FROM UserAgent ua WHERE ua.hash = :hash")
    fun findByHash(hash: Array<Byte>): List<UserAgent>
}
