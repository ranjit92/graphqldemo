package com.graphql.service.datafetcher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.graphql.model.Book;
import com.graphql.repository.BookRepository;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Component
public class AllBookDataFetcher implements DataFetcher<List<Book>>{

	@Autowired
	BookRepository bookRepo;
	
	@Override
	public List<Book> get(DataFetchingEnvironment environment) {
		return bookRepo.findAll();
	}

}
