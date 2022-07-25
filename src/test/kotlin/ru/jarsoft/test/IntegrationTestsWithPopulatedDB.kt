package ru.jarsoft.test

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers
import ru.jarsoft.test.dto.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("testcontainers")
class IntegrationTestsWithPopulatedDB {

    @LocalServerPort
    val port = 0
    lateinit var requestSender: RequestSender

    @Autowired
    lateinit var cleaner: DatabaseCleaner

    @AfterEach
    fun cleanUp() {
        cleaner.deleteEverything()
    }

    val catInitList = listOf(
        CategoryWithoutId("Music", "music"),
        CategoryWithoutId("Kitties", "kitties"),
        CategoryWithoutId("IT", "it"),
        CategoryWithoutId("Video games", "gaming"),
        CategoryWithoutId("Personal Computers", "pc"),
        CategoryWithoutId("Cryptocurrency", "crypto")
    )

    val bannerInitList = listOf(
        BannerWithoutId("Awesome banner", "Lorem Ipsum", 9.99, listOf()),
        BannerWithoutId("Cool gaming PC", "RTX 3080 and AMD 5900X in one PC! Get Now!", 4.2, listOf()),
        BannerWithoutId("New videogame about kitties!",
            "New videogame about kitties in development! Kickstarter is open!", 1.23, listOf()),
        BannerWithoutId("Programmers needed",
            "Want to get a job in IT? Click here to learn more", 13.37, listOf())
    )

    val catBannerInitList = listOf(
        listOf("music", "it"),
        listOf("gaming", "pc"),
        listOf("kitties", "gaming"),
        listOf("it")
    )

    lateinit var catList: List<CategoryDto>
    lateinit var bannerList: List<BannerDto>

    @BeforeEach
    fun populateDB() {
        requestSender = RequestSender(port)

        catInitList.forEach {
            requestSender.createCategory(it)
        }
        catList = requestSender.getAllCategories()
        println(catList)

        for (i in 0..3) {
            val currBanner = bannerInitList[i]
            val newBanner = BannerWithoutId(
                currBanner.name,
                currBanner.text,
                currBanner.price,
                catList
                    .filter { catBannerInitList[i].contains(it.requestId) }
                    .map { it.id }
            )
            requestSender.createBanner(newBanner)
        }
        bannerList = requestSender.getAllBanners()
    }

    @Test
    fun givenPopulatedDB_whenRemoveCategory_thenGetConflict409() {
        val response = requestSender.deleteCategory(catList[0].id)
        assert(response!!.statusCode == HttpStatus.CONFLICT) {
            println("Expected to get 409 Conflict, actual response:")
            println(response.statusCode)
            println(response.body)
        }
    }

    @Test
    fun givenPopulatedDB_whenRemoveBannerThenRemoveCategory_thenBothAreRemoved() {
        // remove kitty videogame banner
        val bannerId = bannerList.first{ it.name == bannerInitList[2].name }.id
        requestSender.deleteBanner(bannerId)

        // remove kitties category
        val catId = catList.first { it.requestId == "kitties" }.id
        requestSender.deleteCategory(catId)

        val restTemplate = TestRestTemplate()
        val headers = HttpHeaders()
        val entity = HttpEntity(null, headers)

        val bannerResponse = restTemplate.exchange(
            requestSender.createURL("/banner/$bannerId"),
            HttpMethod.GET,
            entity,
            String::class.java
        )
        val categoryResponse = restTemplate.exchange(
            requestSender.createURL("/category/$catId"),
            HttpMethod.GET,
            entity,
            String::class.java
        )

        assert(
            bannerResponse.statusCode == HttpStatus.NOT_FOUND &&
            categoryResponse.statusCode == HttpStatus.NOT_FOUND
        ) {
            println("Expected 404 Not Found on both responses")
            println("Actual responses:")
            println("bannerResponse:")
            println(bannerResponse.statusCode)
            println(bannerResponse.body)
            println("categoryResponse:")
            println(categoryResponse.statusCode)
            println(categoryResponse.body)
        }
    }

    @Test
    fun givenPopulatedDB_whenGetBannerIdsAndNames_thenTheyAreConsistent() {
        val idnames = requestSender.getBannerIdNames()
        val expected = bannerList.map { IdName(it.id, it.name) }
        assert(idnames.containsAll(expected) && expected.containsAll(idnames)) {
            println("expected:")
            expected.forEach {
                println(it)
            }
            println("actual:")
            idnames.forEach {
                println(it)
            }
        }
    }

    @Test
    fun givenPopulatedDB_whenUpdateCategory_thenItIsUpdated() {
        val requestId = "kitties"
        val newName = "Cats"
        val newRequestId = "cats"
        val catId = catList.first { it.requestId == requestId }.id

        requestSender.updateCategory(catId, newName, newRequestId)
        val category = requestSender.getCategory(catId)
        assert(category.name == newName && category.requestId == newRequestId) {
            println("Expected: name - $newName, requestId - $newRequestId")
            println("Actual: $category")
        }
    }

    @Test
    fun givenPopulatedDB_whenUpdateCategoryWithNonUniqueName_thenGetConflict() {
        val requestId = "crypto"
        val newName = "IT"
        val cat = catList.first { it.requestId == requestId }

        val response = requestSender.updateCategory(cat.id, newName, cat.requestId)
        assert(response!!.statusCode == HttpStatus.CONFLICT)
    }

    @Test
    fun givenPopulatedDB_whenUpdateCategoryWithNonUniqueRequestId_thenGetConflict() {
        val requestId = "crypto"
        val newRequestId = "it"
        val cat = catList.first { it.requestId == requestId }

        val response = requestSender.updateCategory(cat.id, cat.name, newRequestId)
        assert(response!!.statusCode == HttpStatus.CONFLICT)
    }

    @Test
    fun givenPopulatedDB_whenDeleteCategoryTwice_thenGetNotFound() {
        val requestId = "crypto"
        val catId = catList.first { it.requestId == requestId }.id

        requestSender.deleteCategory(catId)
        val response = requestSender.deleteCategory(catId)
        assert(response!!.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun givenPopulatedDB_whenCreateCategoryWithNonUniqueName_thenGetConflict() {
        val name = "Cryptocurrency"
        val requestId = "cryptocurr"

        val response = requestSender.createCategory(name, requestId)
        assert(response.statusCode == HttpStatus.CONFLICT)
    }

    @Test
    fun givenPopulatedDB_whenCreateCategoryWithNonUniqueRequestId_thenGetConflict() {
        val name = "DogeCoin"
        val requestId = "crypto"

        val response = requestSender.createCategory(name, requestId)
        assert(response.statusCode == HttpStatus.CONFLICT)
    }

    @Test
    fun givenPopulatedDB_whenCreateBannerWithNonUniqueName_thenGetConflict() {
        val newBanner = BannerWithoutId("Awesome banner", "Cool text", 1.11, listOf())
        val response = requestSender.createBanner(newBanner)
        assert(response!!.statusCode == HttpStatus.CONFLICT)
    }

    @Test
    fun givenPopulatedDB_whenUpdateBanner_itIsUpdated() {
        val name = "Awesome banner"
        val banner = bannerList.first { it.name == name }

        val newBanner = BannerWithoutId(
            "Really awesome banner",
            "Sample text",
            1.11,
            banner.categories.map { it.id } + catList.first { it.requestId == "pc" }.id)
        requestSender.updateBanner(banner.id, newBanner)
        val actualNewBanner = requestSender.getBanner(banner.id)
        assert(newBanner.name == actualNewBanner.name)
        assert(newBanner.text == actualNewBanner.text)
        assert(newBanner.price == actualNewBanner.price)
        assert(newBanner.categories.containsAll(actualNewBanner.categories.map { it.id }))
        assert(actualNewBanner.categories.map { it.id }.containsAll(newBanner.categories))
    }

    @Test
    fun givenPopulatedDB_whenUpdateBannerWithNonUniqueName_thenGetConflict() {
        val name = "Awesome banner"
        val banner = bannerList.first { it.name == name }

        val newBanner = BannerWithoutId(
            "Cool gaming PC",
            "Sample text",
            1.11,
            banner.categories.map { it.id } + catList.first { it.requestId == "pc" }.id
        )
        val response = requestSender.updateBanner(banner.id, newBanner)
        assert(response!!.statusCode == HttpStatus.CONFLICT) {
            println("Response was not 409 Conflict, actual response:")
            println(response.statusCode)
            println(response.body)
        }
    }

    @Test
    fun givenPopulatedDB_whenBid_thenGetBanner() {
        val categories = listOf("kitties")
        val response = requestSender.getBid(categories)
        assert(response!!.body ==
                bannerList
                    .first { dto ->
                        dto.categories.map{ it.requestId }.contains(categories[0])
                    }.text)
    }

    @Test
    fun givenPopulatedDB_whenBid_thenGetMostExpensiveBanner() {
        val categories = listOf("it")
        val response = requestSender.getBid(categories)
        val expectedPrice = bannerList
            .filter { dto ->
                dto.categories.map{ it.requestId }.contains(categories[0])
            }.maxOf { it.price }
        val expectedText =
            bannerList
            .filter { dto ->
                dto.categories.map{ it.requestId }.contains(categories[0])
            }.first { it.price == expectedPrice}.text

        assert(response!!.body == expectedText)
    }

    @Test
    fun givenPopulatedDB_whenBidCategoryWithoutBanners_thenGetNoContent() {
        val categories = listOf("crypto")
        val response = requestSender.getBid(categories)
        assert(response!!.statusCode == HttpStatus.NO_CONTENT)
    }
}
