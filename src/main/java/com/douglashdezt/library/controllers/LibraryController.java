package com.douglashdezt.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.models.entities.BookLoan;
import com.douglashdezt.library.models.entities.User;
import com.douglashdezt.library.services.BookLoanService;
import com.douglashdezt.library.services.BookService;
import com.douglashdezt.library.services.UserService;

@RestController
@RequestMapping("/library")
public class LibraryController {

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookLoanService bookLoanService;

	@GetMapping("/books")
	public ResponseEntity<List<Book>> findAllBooks() {
		try {
			System.out.println(userService.getUserAuth());
			List<Book> books = bookService.findAll();

			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/book/{isbn}")
	public ResponseEntity<Book> getBookByIsbn(@PathVariable("isbn") String isbn) {
		try {
			Book foundBook = bookService.findOneByIsbn(isbn);

			if (foundBook == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(foundBook, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/loan/{isbn}")
	public ResponseEntity<?> loanABook(@PathVariable(name = "isbn") String isbn) {
		try {
			String username = userService.getUserAuth();
			User user = userService.findOneByIdentifer(username);

			Book book = bookService.findOneByIsbn(isbn);

			if (book == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Integer activeLoansQnt = bookLoanService.getBookActiveLoans(book).size();

			if (activeLoansQnt >= book.getStock()) {
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);
			}
			
			bookLoanService.create(user, book);
			
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/loans")
	public ResponseEntity<?> getMyLoans(){
		try {
			String username = userService.getUserAuth();
			User user = userService.findOneByIdentifer(username);
			
			List<BookLoan> loans = bookLoanService.getUserActiveLoans(user);
			
			return new ResponseEntity<>(loans, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/return/{isbn}")
	public ResponseEntity<?> returnABook(@PathVariable(name = "isbn") String isbn) {
		try {
			String username = userService.getUserAuth();
			User user = userService.findOneByIdentifer(username);

			Book book = bookService.findOneByIsbn(isbn);

			if (book == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			
			BookLoan loan = bookLoanService.getLoanOf(user, book);
			
			if (loan == null) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			
			bookLoanService.returnBook(loan);
			
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
