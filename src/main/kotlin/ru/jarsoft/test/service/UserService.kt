package ru.jarsoft.test.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.jarsoft.test.NotFoundException
import ru.jarsoft.test.entity.User
import ru.jarsoft.test.repository.UserRepository

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
): UserDetailsService {
    fun register(name: String, password: String) {
        userRepository.save(
            User(
                0,
                name,
                passwordEncoder.encode(password),
                true
            )
        )
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository
            .findByName(username)
            .orElseThrow { NotFoundException() }
            .toPrincipal()
    }
}