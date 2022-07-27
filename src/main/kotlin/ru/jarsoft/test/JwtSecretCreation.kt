package ru.jarsoft.test

import org.springframework.security.crypto.keygen.KeyGenerators
import java.util.*

// Creates a pair of hex random bytes and base64 representation to use as JWT secret
fun main() {
    val key = KeyGenerators.secureRandom(64).generateKey()
    val hex = HexFormat.of().formatHex(key)
    println(hex)
    val encoded = Base64.getEncoder().encodeToString(key)
    println(encoded)
}
