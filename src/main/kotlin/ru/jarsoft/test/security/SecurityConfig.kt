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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.CorsConfigurationSource
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
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*", "http://localhost:3000", "http://localhost:3000/")
        configuration.allowedMethods = listOf("GET", "POST", "DELETE", "PUT", "OPTIONS")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun permitAccessToBidAndLogin(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        jwtAuthorizationFilter: JwtAuthorizationFilter,
        corsConfigurationSource: CorsConfigurationSource,
        loginCorsFilter: LoginCorsFilter
    ): SecurityFilterChain {
        http.csrf().disable()
            .cors()
            .configurationSource(corsConfigurationSource)
            .and()
            .httpBasic().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/bid", "/api/login", "/register")
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
            .addFilterAfter(loginCorsFilter, JwtAuthorizationFilter::class.java)
        return http.build()
    }
}