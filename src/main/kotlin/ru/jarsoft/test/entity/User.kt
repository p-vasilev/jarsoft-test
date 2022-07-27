package ru.jarsoft.test.entity

import ru.jarsoft.test.security.UserPrincipal
import javax.persistence.*

@Entity
@Table(name = "user")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long,

    @Column(name = "name", length = 32)
    var name: String,

    @Column(name = "password", length = 128)
    var password: String,

    @Column(name = "enabled")
    var enabled: Boolean
) {
    fun toPrincipal(): UserPrincipal {
        return UserPrincipal(
            this.name,
            this.password,
            mutableListOf(),
            this.enabled
        )
    }
}