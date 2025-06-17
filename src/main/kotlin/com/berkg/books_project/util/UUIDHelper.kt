package com.berkg.books_project.util

import java.util.UUID

object UUIDHelper {
    fun fromString(uuid: String?): UUID? {
        return try {
            if (uuid == null) null else UUID.fromString(uuid)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
    fun random(): UUID = UUID.randomUUID()
}
