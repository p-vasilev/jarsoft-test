package ru.jarsoft.test

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
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
import ru.jarsoft.test.dto.BannerDto
import ru.jarsoft.test.dto.BannerWithoutId
import ru.jarsoft.test.dto.CategoryDto


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("testcontainers")
class JarsoftTestApplicationIntegrationTests {


    val restTemplate = TestRestTemplate()
    val headers = HttpHeaders()
    @LocalServerPort
    val port = 0

    fun createURL(uri: String) = "http://localhost:$port$uri"

    fun sendCreateCategoryRequest(name: String, requestId: String) {
        val entity = HttpEntity(null, headers)
        restTemplate.exchange(
            createURL("/category/new?name=$name&requestId=$requestId"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )
    }

    fun sendCreateBannerRequest(
        name: String,
        text: String,
        price: Double,
        catIds: List<Long>
    ) {
        val entity2 = HttpEntity(
            Json.encodeToString(
                BannerWithoutId.serializer(),
                BannerWithoutId(
                    name,
                    text,
                    price,
                    catIds
                )
            ),
            headers
        )
        restTemplate.exchange(
            createURL("/banner/new"),
            HttpMethod.PUT,
            entity2,
            String::class.java
        )
    }

    fun sendGetAllCategoriesRequest(): List<CategoryDto> {
        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            createURL("/category/all"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        val categoryDtos = Json.parseToJsonElement(response.body!!).jsonArray.map {
            Json.decodeFromJsonElement(
                CategoryDto.serializer(),
                it
            )
        }
        return categoryDtos
    }

    fun sendGetAllBannersRequest(): List<BannerDto> {
        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            createURL("/banner/all"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        val bannerDtos = Json.parseToJsonElement(response.body!!).jsonArray.map {
            Json.decodeFromJsonElement(
                BannerDto.serializer(),
                it
            )
        }

        return bannerDtos
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
        sendCreateCategoryRequest(catName, requestId)

        val categoryDtos = sendGetAllCategoriesRequest()

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
        sendCreateCategoryRequest(catName, requestId)

        val categoryDtos = sendGetAllCategoriesRequest()

        val catId = categoryDtos.first {
            it.name == catName &&
            it.requestId == requestId
        }.id

        val bannerName = "awesome banner"
        val bannerText = "Lorem ipsum"
        val bannerPrice = 0.42

        // request to add banner
        sendCreateBannerRequest(
            bannerName,
            bannerText,
            bannerPrice,
            listOf(catId)
        )

        // get all banners
        val bannerDtos = sendGetAllBannersRequest()

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
            createURL("/category/new?name=$catName&requestId=$requestId"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )

        // get cat id
        val response = restTemplate.exchange(
            createURL("/category/all"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        val categoryDtos = Json.parseToJsonElement(response.body!!).jsonArray.map {
            Json.decodeFromJsonElement(
                CategoryDto.serializer(),
                it
            )
        }

        val catId = categoryDtos.first {
            it.name == catName &&
            it.requestId == requestId
        }.id

        // remove category
        restTemplate.exchange(
            createURL("/category/delete?id=$catId"),
            HttpMethod.DELETE,
            entity,
            String::class.java
        )

        // get categories
        val response2 = restTemplate.exchange(
            createURL("/category/all"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        // should be empty
        assert(
            response.body == null ||
            response.body!!.isEmpty() ||
            Json.parseToJsonElement(response2.body!!).jsonArray.isEmpty()
        )
    }


}
