package com.berkg.books_project.controller

import com.berkg.books_project.security.JwtService
import com.berkg.books_project.service.UserService
import com.berkg.books_project.service.TokenBlacklistService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val tokenBlacklistService: TokenBlacklistService
) {
    data class RegisterRequest(
        val username: String,
        val password: String,
        val email: String
    )

    data class LoginRequest(
        val username: String,
        val password: String
    )

    data class AuthResponse(
        val accessToken: String,
        val refreshToken: String
    )

    data class RefreshTokenRequest(
        val refreshToken: String
    )

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        try {
            val user = userService.createUser(
                username = request.username,
                password = request.password,
                email = request.email
            )
            val jwtToken = jwtService.generateToken(user.username)
            val refreshToken = jwtService.generateRefreshToken(user.username)
            return ResponseEntity.ok(AuthResponse(jwtToken, refreshToken))
        } catch (e: IllegalArgumentException) {
            // handle validation errors (username/email already exists)
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            // handle other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val jwtToken = jwtService.generateToken(request.username)
        val refreshToken = jwtService.generateRefreshToken(request.username)
        return ResponseEntity.ok(AuthResponse(jwtToken, refreshToken))
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<AuthResponse> {
        try {
            val username = jwtService.extractUsername(request.refreshToken)
            val userDetails = userService.loadUserByUsername(username)
            if (jwtService.isRefreshTokenValid(request.refreshToken, userDetails)) {
                val newAccessToken = jwtService.generateToken(username)
                val newRefreshToken = jwtService.generateRefreshToken(username)
                return ResponseEntity.ok(AuthResponse(newAccessToken, newRefreshToken))
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            val expiration = jwtService.getExpirationMillis(token)
            tokenBlacklistService.blacklistToken(token, expiration)
        }
        return ResponseEntity.ok(mapOf("message" to "Logged out successfully. The old token is blacklisted."))
    }
}
