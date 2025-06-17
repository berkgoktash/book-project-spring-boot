package com.berkg.books_project.config

import com.berkg.books_project.security.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfig {

    @Bean
    fun jwtUtil(): JwtUtil {
        return JwtUtil()
    }
}
