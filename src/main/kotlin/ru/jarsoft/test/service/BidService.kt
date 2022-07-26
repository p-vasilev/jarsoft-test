package ru.jarsoft.test.service

import org.springframework.stereotype.Service
import ru.jarsoft.test.NoContentException
import ru.jarsoft.test.entity.Banner
import ru.jarsoft.test.entity.Log
import ru.jarsoft.test.entity.UserAgent
import ru.jarsoft.test.repository.BannerRepository
import ru.jarsoft.test.repository.CategoryRepository
import ru.jarsoft.test.repository.LogRepository
import ru.jarsoft.test.repository.UserAgentRepository
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.servlet.http.HttpServletRequest

@Service
class BidService(
    val bannerRepository: BannerRepository,
    val logRepository: LogRepository,
    val userAgentRepository: UserAgentRepository,
    val categoryRepository: CategoryRepository
) {
    fun getBanner(categories: List<String>, request: HttpServletRequest): String {
        var banners = bannerRepository.findByCategoryRequestIds(categories)

        val ip: Array<Byte> = request.remoteAddr.split(".").map { it.toByte() }.toTypedArray()

        val uaString = request.getHeader("User-Agent")
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(uaString.toByteArray()).toTypedArray()

        if (banners.isEmpty()) {
            logWithReason("No banner for given categories", hash, uaString, ip)
            throw NoContentException()
        }

        val logs =
            logRepository
                .findByUserAgentAndIpHashWithNotNullBanner(hash, ip)
                .filter {
                    LocalDateTime.ofInstant(it.requestTime, ZoneId.systemDefault()).toLocalDate() ==
                    LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toLocalDate()
                }

        banners = banners.filterNot { logs.map { log -> log.selectedBanner!!.id }.contains(it.id) }

        if (banners.isEmpty()) {
            logWithReason("Banners for given categories already shown to the user today", hash, uaString, ip)
            throw NoContentException()
        }

        val result = banners.maxByOrNull { it.price }!!
        logWithBanner(result, hash, uaString, ip)
        return result.text

    }

    private fun logWithReason(
        reason: String,
        hash: Array<Byte>,
        uaString: String,
        ip: Array<Byte>
    ) {
        val userAgents = userAgentRepository.findByHash(hash)
        var userAgent: UserAgent

        if (userAgents.size == 1) {
            // this ua already exists
            userAgent = userAgents.first()
        } else if (userAgents.isEmpty()) {
            // doesnt exist - create new
            userAgent = UserAgent(0, hash, uaString)
            val uaId = userAgentRepository.save(userAgent).id
            userAgent = UserAgent(uaId, hash, uaString)
        } else {
            // hash collision occurred
            val userAgent2 = userAgents.find { it.string == uaString }
            // not guaranteed that the same user agent is in db (again, because of hash collisions)
            if (userAgent2 == null) {
                userAgent = UserAgent(0, hash, uaString)
                val uaId = userAgentRepository.save(userAgent).id
                userAgent = UserAgent(uaId, hash, uaString)
            } else {
                userAgent = userAgent2
            }
        }


        val log = Log(
            0,
            ip,
            userAgent,
            Instant.now(),
            null,
            null,
            null,
            reason
        )
        logRepository.save(log)
    }

    private fun logWithBanner(
        banner: Banner,
        hash: Array<Byte>,
        uaString: String,
        ip: Array<Byte>
    ) {
        var userAgent = UserAgent(0, hash, uaString)
        val uaId = userAgentRepository.save(userAgent).id
        userAgent = UserAgent(uaId, hash, uaString)

        // have to create brand-new category list
        // this is because hibernate outsmarts itself in a way
        // if we just use banner.categories in log constructor and then save
        // it thinks that the ownership of the categories change
        // and deletes them from banner_category
        val newCategories = banner.categories.map { categoryRepository.findById(it.id).get() }

        val log = Log(
            0,
            ip,
            userAgent,
            Instant.now(),
            banner,
            newCategories,
            banner.price,
            null
        )
        logRepository.save(log)

    }

}
