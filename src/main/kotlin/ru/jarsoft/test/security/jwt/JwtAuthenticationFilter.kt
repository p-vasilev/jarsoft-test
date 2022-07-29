package ru.jarsoft.test.security.jwt

import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import ru.jarsoft.test.dto.Login
import ru.jarsoft.test.security.UserPrincipal
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter: UsernamePasswordAuthenticationFilter() {

    init {
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/api/login", "POST"))
    }

    @Autowired
    lateinit var jwtProvider: JwtProvider

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val requestBody = request.reader.readText()
        val login = Json.decodeFromString(
            Login.serializer(),
            requestBody
        )
        return UsernamePasswordAuthenticationToken(
            UserPrincipal(
                login.username,
                login.password,
                mutableListOf(),
                true
            ),
            null
        )
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        response.addHeader("JWT", jwtProvider.generateToken(authentication))
    }
}