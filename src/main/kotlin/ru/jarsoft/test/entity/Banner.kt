package ru.jarsoft.test.entity

import javax.persistence.*

@Entity
@Table(name="banner")
class Banner (
    @Id
    @GeneratedValue
    var id: Long,

    @Column(unique = true)
    var name: String,

    var text: String,

    var price: Double,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="banner_category",
        joinColumns = [JoinColumn(name = "banner_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "category_id", referencedColumnName = "id")]
    )
    var categories: List<Category>,

    var valid: Boolean
)
