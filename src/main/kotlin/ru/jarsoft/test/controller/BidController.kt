package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.*
import ru.jarsoft.test.service.BidService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/bid")
class BidController(
    val service: BidService,
    val request: HttpServletRequest
) {
    @CrossOrigin
    @GetMapping
    fun getBanner(
        @RequestParam("cat") categories: List<String>
    ): String {
        return service.getBanner(categories, request)
    }
}