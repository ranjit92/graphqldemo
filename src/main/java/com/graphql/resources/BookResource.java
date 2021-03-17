package com.graphql.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.graphql.service.GraphqlService;

import graphql.ExecutionResult;

@RestController
//@RequestMapping("")
public class BookResource {

	@Autowired
	GraphqlService gqlService;
	
	@PostMapping("/books")
	public ResponseEntity<Object> getAllBooks(@RequestBody String query) {
		ExecutionResult er = gqlService.getGraphql().execute(query);
		return new ResponseEntity<>(er, HttpStatus.OK);
	}
	
	@GetMapping("/ping")
	public String getPing() {
		return "Connected";
	}
	
}
