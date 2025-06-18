package com.berkg.books_project.service

import com.berkg.books_project.exception.GlobalExceptionHandler
import com.berkg.books_project.model.Book
import com.berkg.books_project.model.User
import com.berkg.books_project.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.NoSuchElementException

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional
    fun createBook(book: Book, owner: User): Book {
        book.owner = owner
        return bookRepository.save(book)
    }

    @Transactional(readOnly = true)
    fun getBook(id: UUID): Book {
        return bookRepository.findById(id)
            .orElseThrow { NoSuchElementException("Book not found with id: $id") }
    }

    @Transactional(readOnly = true)
    fun getAllBooks(): List<Book> {
        return bookRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getBooksByOwner(owner: User): List<Book> {
        return bookRepository.findByOwner(owner)
    }

    @Transactional(readOnly = true)
    fun getBookByOwner(id: UUID, owner: User): Book {
        return bookRepository.findByIdAndOwner(id, owner)
            .orElseThrow { NoSuchElementException("Book not found with id: $id") }
    }

    @Transactional(readOnly = true)
    fun searchBooksByTitle(title: String): List<Book> {
        return bookRepository.findByTitleContainingIgnoreCase(title)
    }

    @Transactional(readOnly = true)
    fun searchBooksByAuthor(author: String): List<Book> {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
    }

    @Transactional
    fun updateBook(id: UUID, updatedBook: Book, owner: User): Book {
        val existingBook = getBookByOwner(id, owner)
        
        existingBook.apply {
            title = updatedBook.title
            author = updatedBook.author
            description = updatedBook.description
            pageCount = updatedBook.pageCount
            releaseYear = updatedBook.releaseYear
        }
        
        return bookRepository.save(existingBook)
    }

    @Transactional
    fun deleteBook(id: UUID, owner: User) {
        val book = getBookByOwner(id, owner)
        bookRepository.delete(book)
    }
}
