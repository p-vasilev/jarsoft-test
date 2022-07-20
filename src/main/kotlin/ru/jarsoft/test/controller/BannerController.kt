package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.RestController
import ru.jarsoft.test.service.BannerService

@RestController("/banner")
class BannerController(
    service: BannerService
) {

}
