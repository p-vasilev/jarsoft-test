package ru.jarsoft.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("testcontainers")
class JarsoftTestApplicationIntegrationTests {


    val restTemplate = TestRestTemplate()
    val headers = HttpHeaders()
    @LocalServerPort
    val port = 0

    fun createURL(uri: String) = "http://localhost:$port$uri"

    @Test
    @Transactional
    fun contextLoads() {
    }

    @Test
    @Transactional
    fun givenEmptyDB_whenAddCategory_thenCategoryIsSaved() {
        val entity = HttpEntity(null, headers)
        val catName = "Music"
        val requestId = "music"
        // send request to save a category
        restTemplate.exchange(
            createURL("/category?name=$catName&requestId=$requestId"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )

        val response = restTemplate.exchange(
            createURL("/category"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        // TODO make proper assertions
        // (in other tests too)
        assert(response.hasBody() && response.body!!.isNotEmpty())
    }

    @Test
    @Transactional
    fun givenEmptyDB_whenAddCategoryAndBanner_thenBannerIsSaved() {
        val entity = HttpEntity(null, headers)
        val catName = "Music"
        val requestId = "music"
        // request to add category
        restTemplate.exchange(
            createURL("/category?name=$catName&requestId=$requestId"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )

        val bannerName = "awesome banner"
        val bannerText = "Lorem ipsum"
        val bannerPrice = "0.42"
        // request to add banner
        restTemplate.exchange(
            createURL("/banner?name=$bannerName&text=$bannerText&price=$bannerPrice"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )

        // get all banners
        val response = restTemplate.exchange(
            createURL("/banner"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        assert(response.hasBody() && response.body!!.isNotBlank())
        println(response.body)
    }

    @Test
    @Transactional
    fun givenEmptyDB_whenAddCategoryAndRemoveIt_thenItIsAbsent() {
        val entity = HttpEntity(null, headers)
        val catName = "Music"
        val requestId = "music"

        // add category
        restTemplate.exchange(
            createURL("/category?name=$catName&requestId=$requestId"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )

        // remove category
        restTemplate.exchange(
            createURL("/category/delete?id=..."),
            HttpMethod.DELETE,
            entity,
            String::class.java
        )

        val response = restTemplate.exchange(
            createURL("/category"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        assert(response.body == null || response.body!!.isEmpty())
    }


}
