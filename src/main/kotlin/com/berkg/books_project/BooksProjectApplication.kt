package com.berkg.books_project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class BooksProjectApplication

fun main(args: Array<String>) {
	runApplication<BooksProjectApplication>(*args)
}
