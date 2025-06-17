package com.berkg.books_project.repository

import com.berkg.books_project.model.Book
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
class BookRepository(private val jdbcTemplate: JdbcTemplate) {
    
    private val bookRowMapper = RowMapper { rs, _ ->
        Book(
            id = rs.getObject("id", UUID::class.java),
            title = rs.getString("title"),
            author = rs.getString("author"),
            description = rs.getString("description"),
            pageCount = rs.getInt("page_count").takeIf { !rs.wasNull() },
            releaseYear = rs.getString("release_year"),
            ownerId = rs.getObject("owner_id", UUID::class.java)
        )
    }

    fun save(book: Book): Book {
        val sql = """
            INSERT INTO books (id, title, author, description, page_count, release_year, owner_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (id) DO UPDATE
            SET title = EXCLUDED.title,
                author = EXCLUDED.author,
                description = EXCLUDED.description,
                page_count = EXCLUDED.page_count,
                release_year = EXCLUDED.release_year,
                owner_id = EXCLUDED.owner_id
            RETURNING *
        """.trimIndent()

        return jdbcTemplate.queryForObject(sql, bookRowMapper,
            book.id, book.title, book.author, book.description,
            book.pageCount, book.releaseYear, book.ownerId)!!
    }

    fun findById(id: UUID): Optional<Book> {
        val sql = "SELECT * FROM books WHERE id = ?"
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(sql, bookRowMapper, id)
        )
    }

    fun findAll(): List<Book> {
        val sql = "SELECT * FROM books"
        return jdbcTemplate.query(sql, bookRowMapper)
    }

    fun findByOwnerId(ownerId: UUID): List<Book> {
        val sql = "SELECT * FROM books WHERE owner_id = ?"
        return jdbcTemplate.query(sql, bookRowMapper, ownerId)
    }

    fun findByIdAndOwnerId(id: UUID, ownerId: UUID): Optional<Book> {
        val sql = "SELECT * FROM books WHERE id = ? AND owner_id = ?"
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(sql, bookRowMapper, id, ownerId)
        )
    }

    fun findByTitleContainingIgnoreCase(title: String): List<Book> {
        val sql = "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(?)"
        return jdbcTemplate.query(sql, bookRowMapper, "%$title%")
    }

    fun findByAuthorContainingIgnoreCase(author: String): List<Book> {
        val sql = "SELECT * FROM books WHERE LOWER(author) LIKE LOWER(?)"
        return jdbcTemplate.query(sql, bookRowMapper, "%$author%")
    }

    fun deleteById(id: UUID) {
        val sql = "DELETE FROM books WHERE id = ?"
        jdbcTemplate.update(sql, id)
    }
}
