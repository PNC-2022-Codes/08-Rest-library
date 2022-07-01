package com.douglashdezt.library.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.douglashdezt.library.models.dtos.PageableDTO;
import com.douglashdezt.library.models.entities.Book;
import com.douglashdezt.library.repositories.BookRepository;
import com.douglashdezt.library.services.BookService;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public Page<Book> findAll(PageableDTO info) {
		PageRequest request = PageRequest
				.of(info.getPage(), info.getLimit(), Sort.by("title").ascending());
		
		return bookRepository
				.findAll(request);
	}

	@Override
	public Book findOneByIsbn(String isbn) {
		return bookRepository.findById(isbn)
				.orElse(null);
	}

}
