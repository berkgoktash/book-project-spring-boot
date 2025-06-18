package com.berkg.books_project.service

import com.berkg.books_project.model.User
import com.berkg.books_project.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    fun createUser(username: String, password: String, email: String): User {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(username)) {
                throw IllegalArgumentException("Username already exists")
            }

            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                throw IllegalArgumentException("Email already exists")
            }

            val encodedPassword = passwordEncoder.encode(password)
            val user = User(username = username, password = encodedPassword, email = email)
            val savedUser = userRepository.save(user)
            logger.debug("Created user: ${savedUser.username} with ID: ${savedUser.id}")
            return savedUser
        } catch (e: Exception) {
            logger.error("Error creating user: ${e.message}", e)
            throw e
        }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: UUID): User {
        return userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found with ID: $id") }
    }

    @Transactional(readOnly = true)
    fun getUserByUsername(username: String): User {
        return userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }
    }
    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = getUserByUsername(username)
        logger.debug("Loaded user: $user")
        return org.springframework.security.core.userdetails.User
            .withUsername(username)
            .password(user.password)
            .roles("USER")
            .build()
    }
}
