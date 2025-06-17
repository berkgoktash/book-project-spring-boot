package com.berkg.books_project.service

import com.berkg.books_project.model.Book
import com.berkg.books_project.model.User
import com.berkg.books_project.repository.BookRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional(readOnly = true)
    fun getAllBooks(): List<Book> = bookRepository.findAll()

    @Transactional(readOnly = true)
    fun getBookById(id: UUID): Book = bookRepository.findById(id)
        .orElseThrow { NoSuchElementException("Book not found with ID: $id") }

    @Transactional
    fun createBook(book: Book, owner: User): Book {
        val newBook = book.copy(ownerId = owner.id)
        return bookRepository.save(newBook)
    }

    @Transactional
    fun updateBook(id: UUID, bookDetails: Book, currentUser: User): Book {
        val existingBook = getBookById(id)

        // Check if the user is the owner
        if (existingBook.ownerId != currentUser.id) {
            throw AccessDeniedException("You can only update books that you own")
        }

        val updatedBook = existingBook.copy(
            title = bookDetails.title,
            author = bookDetails.author,
            description = bookDetails.description,
            pageCount = bookDetails.pageCount,
            releaseYear = bookDetails.releaseYear
        )

        return bookRepository.save(updatedBook)
    }

    @Transactional
    fun deleteBook(id: UUID, currentUser: User) {
        val book = getBookById(id)

        // Check if the user is the owner
        if (book.ownerId != currentUser.id) {
            throw AccessDeniedException("You can only delete books that you own")
        }

        bookRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getBooksForUser(user: User): List<Book> {
        return bookRepository.findByOwnerId(user.id)
    }

}
