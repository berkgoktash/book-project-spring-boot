package com.berkg.books_project.model

import java.util.UUID

data class Book(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var author: String,
    var description: String? = null,
    var pageCount: Int? = null,
    var releaseYear: String? = null,
    var ownerId: UUID? = null
)
