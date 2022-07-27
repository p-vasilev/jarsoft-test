package ru.jarsoft.test.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal (
    private var username: String,
    private var password: String,
    private var authorities: MutableCollection<out GrantedAuthority>,
    private var enabled: Boolean
): UserDetails {

    override fun getAuthorities() = authorities

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = enabled
}