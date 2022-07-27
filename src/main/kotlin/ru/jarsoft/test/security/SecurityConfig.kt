package ru.jarsoft.test.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.jarsoft.test.security.jwt.JwtAuthenticationFilter
import ru.jarsoft.test.security.jwt.JwtAuthorizationFilter
import ru.jarsoft.test.service.UserService

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun jwtAuthenticationFilter(authenticationManager: AuthenticationManager): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter()
        filter.setAuthenticationManager(authenticationManager)

        return filter
    }


    @Bean
    fun daoAuthenticationProvider(
        passwordEncoder: PasswordEncoder,
        userService: UserService
    ): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(userService)
        return provider
    }
    @Bean
    fun authenticatorManagerBean(daoAuthenticationProvider: DaoAuthenticationProvider) =
        ProviderManager(daoAuthenticationProvider)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun permitAccessToBidAndLogin(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter, jwtAuthorizationFilter: JwtAuthorizationFilter): SecurityFilterChain {
        http.csrf().disable()
            .httpBasic().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/bid", "/auth/**", "/register")
            .permitAll()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}