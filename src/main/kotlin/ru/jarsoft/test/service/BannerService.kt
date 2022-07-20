package ru.jarsoft.test.service

import org.springframework.stereotype.Service
import ru.jarsoft.test.repository.BannerRepository

@Service
class BannerService(
    repository: BannerRepository
) {

}
