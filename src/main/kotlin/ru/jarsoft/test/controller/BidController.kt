package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.RestController
import ru.jarsoft.test.service.BidService

@RestController("/bid")
class BidController(
    service: BidService
) {

}