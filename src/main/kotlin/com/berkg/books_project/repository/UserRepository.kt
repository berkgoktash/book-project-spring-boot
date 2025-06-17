package com.berkg.books_project.repository

import com.berkg.books_project.model.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class UserRepository(private val jdbcTemplate: JdbcTemplate) {
    
    private val userRowMapper = RowMapper { rs, _ ->
        User(
            id = rs.getObject("id", UUID::class.java),
            username = rs.getString("username"),
            password = rs.getString("password"),
            email = rs.getString("email"),
            version = rs.getLong("version")
        )
    }

    fun save(user: User): User {
        val sql = """
            INSERT INTO users (id, username, password, email, version)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (id) DO UPDATE
            SET username = EXCLUDED.username,
                password = EXCLUDED.password,
                email = EXCLUDED.email,
                version = users.version + 1
            RETURNING *
        """.trimIndent()

        return jdbcTemplate.queryForObject(sql, userRowMapper,
            user.id, user.username, user.password, user.email, user.version)!!
    }

    fun findById(id: UUID): Optional<User> {
        val sql = "SELECT * FROM users WHERE id = ?"
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(sql, userRowMapper, id)
        )
    }

    fun findByUsername(username: String): Optional<User> {
        val sql = "SELECT * FROM users WHERE username = ?"
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(sql, userRowMapper, username)
        )
    }

    fun existsByUsername(username: String): Boolean {
        val sql = "SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)"
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, username) ?: false
    }

    fun existsByEmail(email: String): Boolean {
        val sql = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)"
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, email) ?: false
    }
}
