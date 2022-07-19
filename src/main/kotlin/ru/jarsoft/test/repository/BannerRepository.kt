package ru.jarsoft.test.repository

import org.springframework.boot.Banner
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BannerRepository: CrudRepository<Banner, Long>
