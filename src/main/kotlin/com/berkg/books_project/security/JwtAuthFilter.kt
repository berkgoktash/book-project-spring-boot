package com.berkg.books_project.security

import com.berkg.books_project.service.UserService
import com.berkg.books_project.service.TokenBlacklistService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val tokenBlacklistService: TokenBlacklistService
) : OncePerRequestFilter() {
    private val log = org.slf4j.LoggerFactory.getLogger(JwtAuthFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.debug("Processing request to: ${request.method} ${request.requestURI}")
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No valid Authorization header found")
            filterChain.doFilter(request, response)
            return
        }

        try {
            val jwt = authHeader.substring(7)

           
            if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                log.debug("JWT token is blacklisted")
                filterChain.doFilter(request, response)
                return
            }

            val username = jwtService.extractUsername(jwt)
            log.debug("Extracted username: $username")

            if (username.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userService.loadUserByUsername(username)
                log.debug("Loaded user details: $userDetails")

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                    log.debug("Security context set with user: $userDetails")
                } else {
                    log.debug("JWT token is not valid")
                }
            } else {
                log.debug("Username is empty or security context already set")
            }

            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            log.error("Error processing JWT token: ${e.message}", e)
            filterChain.doFilter(request, response)
        }
    }
}
