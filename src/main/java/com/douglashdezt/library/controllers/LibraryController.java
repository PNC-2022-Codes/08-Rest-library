package com.douglashdezt.library.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douglashdezt.library.models.dtos.MessageDTO;
import com.douglashdezt.library.models.dtos.PageableDTO;
import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.models.entities.BookLoan;
import com.douglashdezt.library.models.entities.User;
import com.douglashdezt.library.services.BookLoanService;
import com.douglashdezt.library.services.BookService;
import com.douglashdezt.library.services.UserService;

@RestController
@RequestMapping("/library")
@CrossOrigin(origins = "*")
public class LibraryController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookLoanService bookLoanService;
	
	@GetMapping("/books")
	public ResponseEntity<?> findAllBooks(@Valid PageableDTO info, BindingResult result) {	
		
		if(result.hasErrors()) {
			return new ResponseEntity<>(
				null,
				HttpStatus.BAD_REQUEST
			);
		}
		
		try {
			User userAuth = userService.getUserAuthenticated();
			System.out.println(userAuth.getName());
			
			Page<Book> books = bookService.findAll(info);
			
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
