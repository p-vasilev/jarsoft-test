package ru.jarsoft.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JarsoftTestApplication

fun main(args: Array<String>) {
    runApplication<JarsoftTestApplication>(*args)
}
