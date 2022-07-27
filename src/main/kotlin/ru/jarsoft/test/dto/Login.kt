package ru.jarsoft.test.dto

import kotlinx.serialization.Serializable

@Serializable
data class Login (
    val username: String,
    val password: String
)
