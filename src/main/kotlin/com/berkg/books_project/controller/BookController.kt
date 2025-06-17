package com.berkg.books_project.controller

import com.berkg.books_project.model.Book
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
class BookController(private val bookService: BookService, private val userService: UserService) {

    data class BookRequest(
        val title: String,
        val author: String,
        val description: String? = null,
        val pageCount: Int? = null,
        val releaseYear: String? = null
    )

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<Book>> =
        ResponseEntity.ok(bookService.getAllBooks())

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: UUID): ResponseEntity<Book> =
        ResponseEntity.ok(bookService.getBookById(id))

    @GetMapping("/my-books")
    fun getMyBooks(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<Book>> {
        val user = userService.getUserByUsername(userDetails.username)
        return ResponseEntity.ok(bookService.getBooksForUser(user))
    }

    @PostMapping
    fun createBook(
        @RequestBody bookRequest: BookRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Book> {
        val user = userService.getUserByUsername(userDetails.username)

        val book = Book(
            title = bookRequest.title,
            author = bookRequest.author,
            description = bookRequest.description,
            pageCount = bookRequest.pageCount,
            releaseYear = bookRequest.releaseYear
        )

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(bookService.createBook(book, user))
    }

    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable id: UUID,
        @RequestBody bookRequest: BookRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Book> {
        val user = userService.getUserByUsername(userDetails.username)

        val bookDetails = Book(
            title = bookRequest.title,
            author = bookRequest.author,
            description = bookRequest.description,
            pageCount = bookRequest.pageCount,
            releaseYear = bookRequest.releaseYear
        )

        return ResponseEntity.ok(bookService.updateBook(id, bookDetails, user))
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
