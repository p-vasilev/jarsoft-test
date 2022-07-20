package ru.jarsoft.test.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun permitAccessToBidAndLogin(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http
            .authorizeRequests()
            .antMatchers("/bid")
            .permitAll()
        http
            .formLogin()
            .loginPage("/login")
            .permitAll()
        http
            .logout()
            .permitAll()
        return http.build()
    }
}