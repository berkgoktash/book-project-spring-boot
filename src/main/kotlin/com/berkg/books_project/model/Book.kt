package com.berkg.books_project.model

import com.berkg.books_project.model.User
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var author: String = "",

    @Column(length = 1000)
    var description: String? = null,

    @Column(name = "page_count")
    var pageCount: Int? = null,

    @Column(name = "release_year")
    var releaseYear: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: User? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Book) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Book(id=$id, title='$title', author='$author')"
    }
}
