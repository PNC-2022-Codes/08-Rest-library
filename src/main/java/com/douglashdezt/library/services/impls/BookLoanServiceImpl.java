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
public class BookLoanServiceImpl implements BookLoanService{

	@Autowired
	BookLoanRepository bookLoanRepo;
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void create(User user, Book book) throws Exception {
		Date loanDate = new Date();
		
		BookLoan loan 
			= new BookLoan(loanDate, null, book, user);
		
		bookLoanRepo.save(loan);
	}

	@Override
	public void returnBook(BookLoan bookLoan) throws Exception {
		bookLoan.setReturnDate(new Date());
		bookLoanRepo.save(bookLoan);
	}

	@Override
	public BookLoan getLoanOf(User user, Book book) {
		return bookLoanRepo
				.findFirstByBookAndUserAndReturnDateOrderByLoanDateAsc(book, user, null);
	}

	@Override
	public List<BookLoan> getUserActiveLoans(User user) {
		return bookLoanRepo.findByUserAndReturnDateOrderByLoanDateDesc(user, null);
	}

	@Override
	public List<BookLoan> getBookActiveLoans(Book book) {
		return bookLoanRepo.findByBookAndReturnDateOrderByLoanDateDesc(book, null);
	}

}
