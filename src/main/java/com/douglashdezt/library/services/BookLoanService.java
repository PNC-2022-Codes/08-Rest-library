package com.douglashdezt.library.services;

import java.util.List;

import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.models.entities.BookLoan;
import com.douglashdezt.library.models.entities.User;

public interface BookLoanService {
	void create(User user, Book book) throws Exception;
	void returnBook(BookLoan bookLoan) throws Exception;
	
	BookLoan getLoanOf(User user, Book book);
	
	List<BookLoan> getUserActiveLoans(User user) throws Exception;
	List<BookLoan> getBookActiveLoans(Book book) throws Exception;
}
