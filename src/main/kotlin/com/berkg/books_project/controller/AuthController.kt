package com.berkg.books_project.controller

import com.berkg.books_project.model.User
import com.berkg.books_project.security.JwtService
import com.berkg.books_project.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService
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
        val token: String
    )

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val user = userService.createUser(
            username = request.username,
            password = request.password,
            email = request.email
        )
        val token = jwtService.generateToken(user.username)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val token = jwtService.generateToken(request.username)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            jwtService.invalidateToken(token)
        }
        return ResponseEntity.ok(mapOf("message" to "Logged out successfully. Please delete your token on the client side."))
    }
}
