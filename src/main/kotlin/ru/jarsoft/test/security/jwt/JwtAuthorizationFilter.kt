package ru.jarsoft.test.security.jwt

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.jarsoft.test.service.UserService
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthorizationFilter: OncePerRequestFilter() {

    @Autowired
    lateinit var jwtProvider: JwtProvider

    @Autowired
    lateinit var userService: UserService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.toString() in listOf("/login", "/register", "/bid")) {
            filterChain.doFilter(request, response)
        } else {
            val token = extractJwt(request)
            if (token != null && token.isNotBlank()) {
                try {
                    jwtProvider.validateToken(token)
                    val username = jwtProvider.getUsernameFromToken(token)
                    val userDetails: UserDetails = userService.loadUserByUsername(username)
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    SecurityContextHolder.getContext().authentication = authentication
                    filterChain.doFilter(request, response)
                } catch(e: Exception) {
                    logger.error("Error logging in: ${e.message}")
                    response.addHeader("error", e.message)
                    response.status = HttpStatus.FORBIDDEN.value()
                    response.contentType = APPLICATION_JSON_VALUE
                    response.outputStream.print(
                        Json.encodeToString(
                            Json.serializersModule.serializer(),
                            mapOf(Pair("error", e.message))
                        )
                    )
                }
            } else {
                logger.error("Error logging in: JWT is empty")
                response.status = HttpStatus.FORBIDDEN.value()
                response.contentType = APPLICATION_JSON_VALUE
                response.outputStream.print(
                    Json.encodeToString(
                        Json.serializersModule.serializer(),
                        mapOf(Pair("error", "JWT is empty"))
                    )
                )
            }
        }
    }

    private fun extractJwt(request: HttpServletRequest): String? {
        val token = request.getHeader("Authorization")
        if (token != null && token.isNotBlank() && token.startsWith("Bearer ")) {
            return token.drop(7)
        }
        return null
    }
}