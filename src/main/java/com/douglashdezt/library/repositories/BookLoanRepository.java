package com.douglashdezt.library.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.models.entities.BookLoan;
import com.douglashdezt.library.models.entities.User;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long>{
	List<BookLoan> 	findByUserOrderByLoanDateDesc(User user);
	List<BookLoan> 	findByBookOrderByLoanDateDesc(Book book);
	List<BookLoan> 	findByBookAndUserOrderByLoanDateDesc(Book book, User user);
	
	BookLoan 		findFirstByUserAndBookAndReturnDate(User user, Book book,Date returnDate);
	
	List<BookLoan> 	findByUserAndReturnDateOrderByLoanDateDesc(User user, Date returnDate);
	List<BookLoan> 	findByBookAndReturnDateOrderByLoanDateDesc(Book book, Date returnDate);
}
