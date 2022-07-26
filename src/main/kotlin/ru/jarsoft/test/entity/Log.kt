package ru.jarsoft.test.entity

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name="log")
class Log(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0,

    @Column(name = "ip", length = 4)
    var ip: Array<Byte>,

    @ManyToOne
    @JoinColumn(
        name = "user_agent_id",
        referencedColumnName = "id",
        unique = false
    )
    var userAgent: UserAgent,

    @Column(name = "request_time")
    var requestTime: Instant,

    @ManyToOne
    @JoinColumn(
        name = "banner_id",
        referencedColumnName = "id"
    )
    var selectedBanner: Banner?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="log_banner_category",
        joinColumns = [JoinColumn(name = "log_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "category_id", referencedColumnName = "id")],
    )
    var selectedBannerCategories: List<Category>?,

    @Column(name = "price")
    var price: Double?,

    @Column(name = "reason")
    var reason: String?
)
