package ru.jarsoft.test.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.jarsoft.test.entity.UserAgent

@Repository
interface UserAgentRepository: CrudRepository<UserAgent, Long>