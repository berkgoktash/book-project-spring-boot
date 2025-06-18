package com.berkg.books_project.dto

import com.berkg.books_project.model.Book
import java.util.UUID

data class CreateBookDto(
    val title: String,
    val author: String,
    val description: String?,
    val pageCount: Int?,
    val releaseYear: String?
)

data class BookResponseDto(
    val id: UUID?,
    val title: String,
    val author: String,
    val description: String?,
    val pageCount: Int?,
    val releaseYear: String?,
    val ownerUsername: String
) {
    companion object {
        fun fromBook(book: Book): BookResponseDto {
            return BookResponseDto(
                id = book.id,
                title = book.title,
                author = book.author,
                description = book.description,
                pageCount = book.pageCount,
                releaseYear = book.releaseYear,
                ownerUsername = book.owner?.username ?: "Unknown"
            )
        }
    }
}

fun BookResponseDto.fromBook(book: Book) = BookResponseDto(
    id = book.id,
    title = book.title,
    author = book.author,
    description = book.description,
    pageCount = book.pageCount,
    releaseYear = book.releaseYear,
    ownerUsername = book.owner?.username ?: "Unknown"
)