package ru.jarsoft.test.service

import org.springframework.stereotype.Service
import ru.jarsoft.test.repository.BannerRepository
import ru.jarsoft.test.repository.LogRepository

@Service
class BidService(
    bannerRepository: BannerRepository,
    logRepository: LogRepository
) {

}
