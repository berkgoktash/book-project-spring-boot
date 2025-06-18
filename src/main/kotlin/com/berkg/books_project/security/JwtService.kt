package com.berkg.books_project.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date

@Service
class JwtService {
    private val logger = org.slf4j.LoggerFactory.getLogger(JwtService::class.java)
    private val secretKey = "thisisaverysecureandlongkeythatwillbeusedforjwttokensignature123456789"
    private val validityInMs = 3600000L // 1 hour
    private val refreshExpirationInMs = 86400000L // 1 day

    fun extractUsername(token: String): String {
        val username = extractClaim(token) { obj: Claims -> obj.subject }
        logger.debug("Extracted username from token: $username")
        return username
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun generateToken(username: String): String {
        return generateToken(mapOf(), username)
    }

    fun generateToken(extraClaims: Map<String, Any>, username: String): String {
        return buildToken(extraClaims, username, validityInMs)
    }
    fun generateRefreshToken(username: String): String {
        return buildToken(mapOf(), username, refreshExpirationInMs)
    }
    fun buildToken(extraClaims: Map<String, Any>, username: String, expiration: Long): String {
        val now = Date()
        val validity = Date(now.time + expiration)

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        val isValid = (username == userDetails.username) && !isTokenExpired(token)
        logger.debug("Token validation - Username from token: $username, Username from userDetails: ${userDetails.username}, Is expired: ${isTokenExpired(token)}, Is valid: $isValid")
        return isValid
    }

    fun isRefreshTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        val isValid = (username == userDetails.username) && !isTokenExpired(token)
        logger.debug("Refresh token validation - Username from token: $username, Username from userDetails: ${userDetails.username}, Is expired: ${isTokenExpired(token)}, Is valid: $isValid")
        return isValid
    }

    fun invalidateToken(token: String) {
        // Since we're using stateless JWT, we can't actually invalidate the token
        // The token will remain valid until it expires
        // In a real application, you might want to implement a token blacklist
        // or use a shorter expiration time
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSigningKey(): Key {
        return Keys.hmacShaKeyFor(secretKey.toByteArray())
    }
}
