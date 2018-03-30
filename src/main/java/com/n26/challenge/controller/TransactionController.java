package com.n26.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.service.TransactionService;

@RestController
public class TransactionController {
	
	@Autowired private TransactionService service;
	
	@PostMapping("/transactions")
	public void putTransaction(ServerHttpResponse response, @RequestBody Transaction transaction) {		
		response.setStatusCode(service.putTransaction(transaction) ? HttpStatus.CREATED : HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/statistics")
	public Statistics getStatistics() {
		return service.getStatistics();
	}
}
