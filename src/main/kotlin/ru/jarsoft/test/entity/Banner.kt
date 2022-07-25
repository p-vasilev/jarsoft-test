package ru.jarsoft.test.entity

import javax.persistence.*

@Entity
@Table(name="banner")
class Banner (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0,

    @Column(name = "name", unique = true)
    var name: String,

    @Column(name = "text")
    var text: String,

    @Column(name = "price")
    var price: Double,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="banner_category",
        joinColumns = [JoinColumn(name = "banner_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "category_id", referencedColumnName = "id")]
    )
    var categories: List<Category>,

    @Column(name = "valid")
    var valid: Boolean
) {
    override fun toString(): String {
        return "Banner(id=$id, name='$name', text='$text', price=$price, valid=$valid)"
    }
}
