package ru.jarsoft.test

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import org.junit.jupiter.api.BeforeAll
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
    lateinit var requestSender: RequestSender

    @BeforeAll
    fun getRequestSender() {
        requestSender = RequestSender(port)
    }

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
        requestSender.createCategory(catName, requestId)

        val categoryDtos = requestSender.getAllCategories()

        // should be 1 category
        assert(
            categoryDtos.count {
                it.name == catName &&
                it.requestId == requestId
            } == 1 &&
            categoryDtos.count() == 1
        )
    }

    @Test
    @Transactional
    fun givenEmptyDB_whenAddCategoryAndBanner_thenBannerIsSaved() {
        val entity = HttpEntity(null, headers)
        val catName = "Music"
        val requestId = "music"
        // request to add category
        requestSender.createCategory(catName, requestId)

        val categoryDtos = requestSender.getAllCategories()

        val catId = categoryDtos.first {
            it.name == catName &&
            it.requestId == requestId
        }.id

        val bannerName = "awesome banner"
        val bannerText = "Lorem ipsum"
        val bannerPrice = 0.42

        // request to add banner
        requestSender.createBanner(
            bannerName,
            bannerText,
            bannerPrice,
            listOf(catId)
        )

        // get all banners
        val bannerDtos = requestSender.getAllBanners()

        // should have 1 banner
        assert(
            bannerDtos.count { dto ->
                dto.name == bannerName &&
                dto.text == bannerText &&
                dto.price == bannerPrice &&
                dto.categories.map { it.id } == listOf(catId)
            } == 1 &&
            bannerDtos.count() == 1
        )
    }

    @Test
    @Transactional
    fun givenEmptyDB_whenAddCategoryAndRemoveIt_thenItIsAbsent() {
        val entity = HttpEntity(null, headers)
        val catName = "Music"
        val requestId = "music"

        // add category
        restTemplate.exchange(
            requestSender.createURL("/category/new?name=$catName&requestId=$requestId"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )

        // get cat id
        val categoryDtos = requestSender.getAllCategories()

        val catId = categoryDtos.first {
            it.name == catName &&
            it.requestId == requestId
        }.id

        // remove category
        requestSender.removeCategory(catId)

        // get categories
        val response = restTemplate.exchange(
            requestSender.createURL("/category/all"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        // should be empty
        assert(
            response.body == null ||
            response.body!!.isEmpty() ||
            Json.parseToJsonElement(response.body!!).jsonArray.isEmpty()
        )
    }

}
