package com.douglashdezt.library.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.douglashdezt.library.models.dtos.PageableDTO;
import com.douglashdezt.library.models.entities.Book;

public interface BookService {
	Page<Book> findAll(PageableDTO info);
	Book findOneByIsbn(String isbn);
}
