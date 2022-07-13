package ru.jarsoft.test.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="user_agent")
class UserAgent (
    @Id
    @GeneratedValue
    var id: Long,

    var hash: ByteArray,

    var string: String
)