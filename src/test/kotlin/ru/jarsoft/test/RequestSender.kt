package ru.jarsoft.test

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import ru.jarsoft.test.dto.*


/**
 * Used to send requests in tests
 * @param port local server port
 */
class RequestSender (private val port: Int) {
    val restTemplate = TestRestTemplate()
    val headers = HttpHeaders()

    fun createURL(uri: String) = "http://localhost:$port$uri"

    fun createCategory(name: String, requestId: String): ResponseEntity<String>? {
        val entity = HttpEntity(null, headers)
        return restTemplate.exchange(
            createURL("/category/new?name=$name&requestId=$requestId"),
            HttpMethod.POST,
            entity,
            String::class.java
        )
    }

    fun createCategory(cat: CategoryWithoutId) = createCategory(cat.name, cat.requestId)

    fun createBanner(
        name: String,
        text: String,
        price: Double,
        catIds: List<Long>
    ): ResponseEntity<String>? {
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
        return restTemplate.exchange(
            createURL("/banner/new"),
            HttpMethod.POST,
            entity2,
            String::class.java
        )
    }

    fun createBanner(banner: BannerWithoutId) = createBanner(banner.name, banner.text, banner.price, banner.categories)

    fun getAllCategories(): List<CategoryDto> {
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

    fun getAllBanners(): List<BannerDto> {
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

    fun removeCategory(id: Long): ResponseEntity<String>? {
        val entity = HttpEntity(null, headers)
        return restTemplate.exchange(
            createURL("/category/$id"),
            HttpMethod.DELETE,
            entity,
            String::class.java
        )
    }

    fun removeBanner(id: Long): ResponseEntity<String>? {
        val entity = HttpEntity(null, headers)
        return restTemplate.exchange(
            createURL("/banner/$id"),
            HttpMethod.DELETE,
            entity,
            String::class.java
        )
    }

    fun getBannerIdNames(): List<IdName> {
        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            createURL("/banner/ids_and_names"),
            HttpMethod.GET,
            entity,
            String::class.java
        )
        return Json.parseToJsonElement(response.body!!).jsonArray.map {
            Json.decodeFromJsonElement(
                IdName.serializer(),
                it
            )
        }
    }

    fun updateCategory(id: Long, name: String, requestId: String): ResponseEntity<String>? {
        val entity = HttpEntity(null, headers)
        return restTemplate.exchange(
            createURL("/category/$id?name=$name&requestId=$requestId"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )
    }

    fun updateCategory(id: Long, category: CategoryWithoutId) = updateCategory(id, category.name, category.requestId)

    fun updateBanner(
        id: Long,
        banner: BannerWithoutId
    ): ResponseEntity<String>? {
        val entity = HttpEntity(
            Json.encodeToString(
                BannerWithoutId.serializer(),
                banner
            ),
            headers)
        return restTemplate.exchange(
            createURL("/banner/$id"),
            HttpMethod.PUT,
            entity,
            String::class.java
        )
    }

    fun updateBanner(
        id: Long,
        name: String,
        text: String,
        price: Double,
        catIds: List<Long>
    ) = updateBanner(id, BannerWithoutId(name, text, price, catIds))

    fun getCategory(id: Long): CategoryDto {
        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            createURL("/category/$id"),
            HttpMethod.GET,
            entity,
            String::class.java
        )
        return Json.decodeFromString(
            CategoryDto.serializer(),
            response.body!!
        )
    }

    fun getBanner(id: Long): BannerDto {
        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            createURL("/banner/$id"),
            HttpMethod.GET,
            entity,
            String::class.java
        )
        return Json.decodeFromString(
            BannerDto.serializer(),
            response.body!!
        )
    }

    fun getBid(categories: List<String>): ResponseEntity<String>? {
        val entity = HttpEntity(null, headers)
        return restTemplate.exchange(
            createURL("/bid?" + categories.map { "cat=$it" }.reduce{ a, b -> "$a&$b" }),
            HttpMethod.GET,
            entity,
            String::class.java
        )
    }

}