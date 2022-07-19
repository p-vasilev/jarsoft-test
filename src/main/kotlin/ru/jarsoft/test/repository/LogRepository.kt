package ru.jarsoft.test.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.jarsoft.test.entity.Log

@Repository
interface LogRepository: CrudRepository<Log, Long>
