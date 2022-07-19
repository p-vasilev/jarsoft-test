package ru.jarsoft.test.entity

import javax.persistence.*

@Entity
@Table(name = "category")
class Category (
    @Id
    @GeneratedValue
    var id: Long,

    @Column(length = 64, unique = true)
    var name: String,

    @Column(length = 64, unique = true)
    var requestId: String,

    var valid: Boolean
)