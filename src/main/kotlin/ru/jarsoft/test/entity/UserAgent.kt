package ru.jarsoft.test.entity

import javax.persistence.*

@Entity
@Table(name="user_agent")
class UserAgent (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0,

    @Column(name = "hash")
    var hash: Array<Byte>,

    @Column(name = "string")
    var string: String
)