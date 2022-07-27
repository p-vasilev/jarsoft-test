package ru.jarsoft.test.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.jarsoft.test.dto.Login
import ru.jarsoft.test.service.UserService

@RestController
class UserController(
    val service: UserService
) {
    @PostMapping("/register")
    fun register(
        @RequestBody login: Login
    ) {
        service.register(login.username, login.password)
    }
}