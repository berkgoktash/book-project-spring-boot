package com.berkg.books_project.model

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val password: String,
    val email: String,
    val version: Long = 0
)
