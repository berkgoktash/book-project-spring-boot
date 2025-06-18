package com.berkg.books_project.repository

import com.berkg.books_project.model.Book
import com.berkg.books_project.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface BookRepository : JpaRepository<Book, UUID> {
    fun findByTitleContainingIgnoreCase(title: String): List<Book>
    fun findByAuthorContainingIgnoreCase(author: String): List<Book>
    fun findByOwner(owner: User): List<Book>
    fun findByIdAndOwner(id: UUID, owner: User): Optional<Book>
}
