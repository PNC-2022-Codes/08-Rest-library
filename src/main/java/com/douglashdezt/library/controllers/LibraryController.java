package com.douglashdezt.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.services.BookService;

@RestController
@RequestMapping("/library")
public class LibraryController {

	@Autowired
	private BookService bookService;
	
	@GetMapping("/books")
	public ResponseEntity<List<Book>> findAllBooks() {
		try {
			List<Book> books = bookService.findAll();
			
			return new ResponseEntity<>(
						books,
						HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
						null,
						HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
	}
	
	@GetMapping("/book/{isbn}")
	public ResponseEntity<Book> getBookByIsbn(@PathVariable("isbn") String isbn) {
		try {
			Book foundBook = bookService.findOneByIsbn(isbn);
			
			if(foundBook == null) {
				return new ResponseEntity<>(
						null,
						HttpStatus.NOT_FOUND
					);
			}
			
			return new ResponseEntity<>(
					foundBook,
					HttpStatus.OK
				);
		} catch (Exception e) {
			return new ResponseEntity<>(
					null,
					HttpStatus.INTERNAL_SERVER_ERROR
				);
		}
	}
}
