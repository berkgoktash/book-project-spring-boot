package com.berkg.books_project.controller

import com.berkg.books_project.dto.BookResponseDto
import com.berkg.books_project.dto.CreateBookDto
import com.berkg.books_project.model.Book
import com.berkg.books_project.model.User
import com.berkg.books_project.service.BookService
import com.berkg.books_project.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService,
    private val userService: UserService
) {

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<BookResponseDto>> {
        val books = bookService.getAllBooks()
        return ResponseEntity.ok(books.map { BookResponseDto.fromBook(it) })
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id: UUID): ResponseEntity<BookResponseDto> {
        val book = bookService.getBook(id)
        return ResponseEntity.ok(BookResponseDto.fromBook(book))
    }

    @GetMapping("/my-books")
    fun getMyBooks(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<BookResponseDto>> {
        val user = userService.getUserByUsername(userDetails.username)
        val books = bookService.getBooksByOwner(user)
        return ResponseEntity.ok(books.map { BookResponseDto.fromBook(it) })
    }

    @GetMapping("/search/title")
    fun searchByTitle(@RequestParam title: String): ResponseEntity<List<BookResponseDto>> {
        val books = bookService.searchBooksByTitle(title)
        return ResponseEntity.ok(books.map { BookResponseDto.fromBook(it) })
    }

    @GetMapping("/search/author")
    fun searchByAuthor(@RequestParam author: String): ResponseEntity<List<BookResponseDto>> {
        val books = bookService.searchBooksByAuthor(author)
        return ResponseEntity.ok(books.map { BookResponseDto.fromBook(it) })
    }

    @PostMapping
    fun createBook(
        @RequestBody bookDto: CreateBookDto,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<BookResponseDto> {
        val user = userService.getUserByUsername(userDetails.username)
        val book = Book(
            title = bookDto.title,
            author = bookDto.author,
            description = bookDto.description,
            pageCount = bookDto.pageCount,
            releaseYear = bookDto.releaseYear
        )
        val createdBook = bookService.createBook(book, user)
        return ResponseEntity.ok(BookResponseDto.fromBook(createdBook))
    }

    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable id: UUID,
        @RequestBody bookDto: CreateBookDto,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<BookResponseDto> {
        val user = userService.getUserByUsername(userDetails.username)
        val book = Book(
            title = bookDto.title,
            author = bookDto.author,
            description = bookDto.description,
            pageCount = bookDto.pageCount,
            releaseYear = bookDto.releaseYear
        )
        val updatedBook = bookService.updateBook(id, book, user)
        return ResponseEntity.ok(BookResponseDto.fromBook(updatedBook))
    }

    @DeleteMapping("/{id}")
    fun deleteBook(
        @PathVariable id: UUID,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Unit> {
        val user = userService.getUserByUsername(userDetails.username)
        bookService.deleteBook(id, user)
        return ResponseEntity.noContent().build()
    }
}
