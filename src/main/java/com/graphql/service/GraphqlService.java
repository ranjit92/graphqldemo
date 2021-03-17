package com.graphql.service;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.graphql.model.Book;
import com.graphql.repository.BookRepository;
import com.graphql.service.datafetcher.AllBookDataFetcher;
import com.graphql.service.datafetcher.BookDataFetcher;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphqlService {

	@Value("classpath:books.graphql")
	Resource resource;
	
	private GraphQL graphql;
	
	@Autowired
	private AllBookDataFetcher allBookDataFetcher;
	
	@Autowired
	private BookDataFetcher bookDataFetcher;
	
	@Autowired
	private BookRepository bookRepo;
	
	@PostConstruct
	private void loadSchema() throws IOException {
		System.out.println("inside loadSchema()");
		loadDataIntoHSQL();
		
		File file = resource.getFile();
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(file);
		RuntimeWiring wiring = buildRunTimelWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
		graphql = GraphQL.newGraphQL(schema).build();
		
		
	}


	private void loadDataIntoHSQL() {
		Stream.of(
				new Book("345","abc", "bcv",new String[] {
						"auth1", "auth2"
				}, "12th Dec"),
				
				new Book("346","abcty", "bcvui",new String[] {
						"auth0", "auth9"
				}, "15th Dec"),
				
				new Book("347","abc", "bcvrtytr",new String[] {
						"auth7", "auth8"
				}, "14th Dec")
				
				
				).forEach(book -> bookRepo.save(book));
		
	}


	private RuntimeWiring buildRunTimelWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("Query", typeWiring ->
					typeWiring
					.dataFetcher("allBooks", allBookDataFetcher)
					.dataFetcher("book", bookDataFetcher))
				.build();
	}
	
	@Bean
	public GraphQL getGraphql() {
		return graphql;
	}
}
