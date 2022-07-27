package ru.jarsoft.test.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ru.jarsoft.test.security.UserPrincipal
import java.util.*

@Component
class JwtProvider {
    @Value("\${app.jwtSecret}")
    lateinit var jwtSecret: String

    private fun getSecretKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))

    @Value("\${app.jwtExpirationInMs}")
    var jwtExpirationInMs: Long = 0L

    fun validateToken(token: String) {
        Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parse(token)
    }

    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
        return claims.body.subject
    }

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal
        val now = Date()
        val expires = Date(now.time + jwtExpirationInMs)
        val key = getSecretKey()

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(now)
            .setExpiration(expires)
            .signWith(key)
            .compact()
    }

}