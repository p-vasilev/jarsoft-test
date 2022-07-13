package ru.jarsoft.test.entity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class BannerCategory (
    @Id
    val bannerId: Long,
    @Id
    val categoryId: Long
)