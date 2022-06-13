package com.douglashdezt.library.services.impls;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.models.entities.BookLoan;
import com.douglashdezt.library.models.entities.User;
import com.douglashdezt.library.repositories.BookLoanRepository;
import com.douglashdezt.library.services.BookLoanService;

@Service
public class BookLoanServiceImpl implements BookLoanService {
	@Autowired
	BookLoanRepository bookLoanRepository;
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void create(User user, Book book) throws Exception {
		Date loanDate = new Date();
		
		BookLoan loan = new BookLoan();
		
		loan.setBook(book);
		loan.setUser(user);
		loan.setLoanDate(loanDate);
		
		bookLoanRepository.save(loan);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void returnBook(BookLoan bookLoan) throws Exception {
		bookLoan.setReturnDate(new Date());
		bookLoanRepository.save(bookLoan);
	}

	@Override
	public List<BookLoan> getUserActiveLoans(User user) throws Exception {
		return bookLoanRepository.findByUserAndReturnDateOrderByLoanDateDesc(user, null);
	}
	
	@Override
	public List<BookLoan> getBookActiveLoans(Book book) throws Exception {
		return bookLoanRepository.findByBookAndReturnDateOrderByLoanDateDesc(book, null);
	}

	@Override
	public BookLoan getLoanOf(User user, Book book) {
		return bookLoanRepository.findFirstByUserAndBookAndReturnDate(user, book, null);
	}
}
