package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.jarsoft.test.service.BidService

@RestController("/bid")
class BidController(
    service: BidService
) {
    @GetMapping
    fun getBanner(
        @RequestParam("cat") categories: List<String>
    ) {
        TODO()
    }
}