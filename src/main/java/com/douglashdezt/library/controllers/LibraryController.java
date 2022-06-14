package com.douglashdezt.library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglashdezt.library.models.dtos.MessageDTO;
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
			User userAuth = userService.getUserAuthenticated();
			System.out.println(userAuth.getName());
			
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
	
	@PostMapping("/loan/{isbn}")
	public ResponseEntity<?> loanABook(@PathVariable(name = "isbn") String isbn) {
		try {
			User user = userService.getUserAuthenticated();
			Book book = bookService.findOneByIsbn(isbn);
			
			if(book == null) {
				return new ResponseEntity<>(
					new MessageDTO("Libro no encontrado"),
					HttpStatus.NOT_FOUND
				);
			}
			
			int activeLoansQnt = bookLoanService
					.getBookActiveLoans(book).size();
			
			if(activeLoansQnt >= book.getStock()) {
				return new ResponseEntity<>(
					new MessageDTO("Sin stock"),
					HttpStatus.CONFLICT
				);
			}
			
			bookLoanService.create(user, book);
			
			return new ResponseEntity<>(
				new MessageDTO("Prestamo registrado"),
				HttpStatus.CREATED
			);
		} catch (Exception e) {
			return new ResponseEntity<>(
				new MessageDTO("Error interno de servidor"),
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}
	
	@GetMapping("/my-loans")
	public ResponseEntity<?> getMyLoans() {
		try {
			User user = userService.getUserAuthenticated();
			List<BookLoan> loans = bookLoanService.getUserActiveLoans(user);
			
			return new ResponseEntity<>(
				loans,
				HttpStatus.OK
			);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
				new MessageDTO("Error interno de servidor"),
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}
	
	@PutMapping("/return/{isbn}")
	public ResponseEntity<?> returnBookToLibrary(@PathVariable(name = "isbn") String isbn) {
		try {
			User user = userService.getUserAuthenticated();
			Book book = bookService.findOneByIsbn(isbn);
			
			if(book == null) {
				return new ResponseEntity<>(
					new MessageDTO("Libro no encontrado"),
					HttpStatus.NOT_FOUND
				);
			}
			
			BookLoan loan = bookLoanService.getLoanOf(user, book);
			
			if(loan == null) {
				return new ResponseEntity<>(
						new MessageDTO("Prestamo no encontrado"),
						HttpStatus.NOT_FOUND
					);
			}
			
			bookLoanService.returnBook(loan);
			
			return new ResponseEntity<>(
					new MessageDTO("Prestamo terminado"),
					HttpStatus.OK
				);
		} catch (Exception e) {
			return new ResponseEntity<>(
				new MessageDTO("Error interno de servidor"),
				HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}
}
