package com.berkg.books_project.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Version
    @Column(nullable = false)
    var version: Long = 0
) {
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val books: MutableList<Book> = mutableListOf()

    constructor() : this(
        username = "",
        password = "",
        email = ""
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', email='$email')"
    }
}
