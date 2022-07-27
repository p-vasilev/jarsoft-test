package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.jarsoft.test.service.BidService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/bid")
class BidController(
    val service: BidService,
    val request: HttpServletRequest
) {
    @GetMapping
    fun getBanner(
        @RequestParam("cat") categories: List<String>
    ): String {
        return service.getBanner(categories, request)
    }
}