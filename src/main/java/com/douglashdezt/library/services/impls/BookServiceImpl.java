package com.douglashdezt.library.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.repositories.BookRepository;
import com.douglashdezt.library.services.BookService;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book findOneByIsbn(String isbn) {
		return bookRepository.findById(isbn)
				.orElse(null);
	}

}
