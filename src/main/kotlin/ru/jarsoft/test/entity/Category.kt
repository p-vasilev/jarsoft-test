package ru.jarsoft.test.entity

import javax.persistence.*

@Entity
@Table(name = "category")
class Category (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0,

    @Column(name = "name", length = 64, unique = true)
    var name: String,

    @Column(name = "request_id", length = 64, unique = true)
    var requestId: String,

    @Column(name = "valid")
    var valid: Boolean
) {
    override fun toString(): String {
        return "Category(id=$id, name='$name', requestId='$requestId', valid=$valid)"
    }
}