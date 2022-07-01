package com.douglashdezt.library.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.douglashdezt.library.models.entities.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, String> {
	
}
