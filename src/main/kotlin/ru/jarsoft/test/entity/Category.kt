package ru.jarsoft.test.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Category (
    @Id
    @GeneratedValue
    var id: Long,

    @Column(length=64)
    var name: String,

    @Column(length=64)
    var requestId: String
)