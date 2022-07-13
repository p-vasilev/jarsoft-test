package ru.jarsoft.test.entity

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name="log")
class Log (
    @Id
    @GeneratedValue
    var id: Long,

    var ip: Int,

    @ManyToOne
    @JoinColumn(
        name="user_agent_id",
        foreignKey = ForeignKey(name="USER_AGENT_ID_FK")
    )
    var userAgent: UserAgent,

    var requestTime: Instant,

    @ManyToOne
    @JoinColumn(
        name="selected_banner_id",
        foreignKey = ForeignKey(name="SELECTED_BANNER_ID_FK")
    )
    var selectedBanner: Banner?,

    var bannerPrice: Double?,

    var reason: String?
)