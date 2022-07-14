package ru.jarsoft.test.entity

import javax.persistence.*

@Entity
@Table(name = "category")
class Category (
    @Id
    @GeneratedValue
    var id: Long,

    @Column(length=64)
    var name: String,

    @Column(length=64)
    var requestId: String
)