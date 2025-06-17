package com.berkg.books_project.controller

import com.berkg.books_project.security.JwtService
import com.berkg.books_project.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService
    ) {

    data class RegisterRequest(val username: String, val password: String, val email: String)
    data class LoginRequest(val username: String, val password: String)
    data class AuthResponse(val token: String, val username: String)

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val user = userService.createUser(request.username, request.password, request.email)
        val token = jwtService.generateToken(user.username)
        return ResponseEntity.ok(AuthResponse(token, user.username))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )

        val token = jwtService.generateToken(request.username)
        return ResponseEntity.ok(AuthResponse(token, request.username))
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("message" to "Logged out successfully. Please delete your token on the client.")) // Tokens are kept client-side
    }
}
