package com.berkg.books_project.config

import com.berkg.books_project.security.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class BeanConfig {

    @Bean
    fun jwtUtil(): JwtUtil {
        return JwtUtil()
    }

    @Bean
    fun stringRedisTemplate(redisConnectionFactory: LettuceConnectionFactory): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory)
    }

    @Bean
    fun lettuceConnectionFactory(
        @Value("\${spring.redis.host}") host: String,
        @Value("\${spring.redis.port}") port: Int
    ): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration()
        config.hostName = host
        config.port = port
        return LettuceConnectionFactory(config)
    }
}
