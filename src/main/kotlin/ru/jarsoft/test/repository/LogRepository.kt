package ru.jarsoft.test.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.jarsoft.test.entity.Log

@Repository
interface LogRepository: CrudRepository<Log, Long> {
    @Query(value = "SELECT l FROM Log l WHERE l.userAgent.hash = :hash AND l.ip = :ip AND l.selectedBanner IS NOT NULL")
    fun findByUserAgentAndIpHashWithNotNullBanner(hash: Array<Byte>, ip: Array<Byte>): List<Log>
}
