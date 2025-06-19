package com.berkg.books_project.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class TokenBlacklistService(private val redisTemplate: StringRedisTemplate) {
    private val blacklistPrefix = "blacklisted_token:"

    fun blacklistToken(token: String, expirationMillis: Long) {
        val key = blacklistPrefix + token
        redisTemplate.opsForValue().set(key, "true", expirationMillis, TimeUnit.MILLISECONDS)
    }

    fun isTokenBlacklisted(token: String): Boolean {
        val key = blacklistPrefix + token
        return redisTemplate.hasKey(key)
    }
} 