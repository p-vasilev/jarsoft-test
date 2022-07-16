package ru.jarsoft.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName


@SpringBootTest
@Testcontainers
@ActiveProfiles("testcontainers")
class JarsoftTestApplicationTests {

    companion object {

        @Container
        val redis: GenericContainer<*> =
            GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
                .withExposedPorts(6379)

        init {
            redis.start()
            System.setProperty("spring.redis.host", redis.host)
            System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString())
        }

    }

    @Test
    fun contextLoads() {
    }

}
