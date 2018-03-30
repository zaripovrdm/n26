package com.n26.challenge.controller;

import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.n26.challenge.AppConfiguration;
import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;

@ContextConfiguration(classes = AppConfiguration.class)
public class TransactionControllerIT extends AbstractTestNGSpringContextTests {
	
	@Autowired private ApplicationContext context;
	
	private WebTestClient webClient;
	
	@BeforeClass
	public void setUp() {
		webClient = WebTestClient.bindToApplicationContext(context).build();
	}
	
	@DataProvider
	public Object[][] transactonProvider() {
		long timestamp = System.currentTimeMillis();
		
		return new Object[][] {
			{new Transaction(70, timestamp - 1000 * 40)},
			{new Transaction(10, timestamp - 1000 * 20)},
			{new Transaction(40, timestamp - 1000 * 20)}
		};
	}
	
	@Test(dataProvider = "transactonProvider")
	public void testPutTransaction(Transaction transaction) { // Put three transaction to storage
		
		webClient
			.post()
			.uri("/transactions")
			.body(BodyInserters.fromObject(transaction))
			.exchange()
			.expectStatus().isCreated();		
	}
	
	@Test
	public void testNegativePutTransaction() { // Trying to put transaction older than 60 seconds
		
		webClient
			.post()
			.uri("/transactions")
			.body(BodyInserters.fromObject(new Transaction(45, System.currentTimeMillis() - 1000 * 70)))
			.exchange()
			.expectStatus().isNoContent();		
	}
	
	@Test(dependsOnMethods = {"testPutTransaction", "testNegativePutTransaction"})
	public void testGetStatistics() { // Here should be statistics based on testPutTransaction() invocation
		
		Statistics stat = webClient
			.get()
			.uri("/statistics")
			.exchange()
			.expectStatus().isOk()
			.expectBody(Statistics.class)
			.returnResult()
			.getResponseBody();
		
		assertEquals(stat.getCount(), 3);
		assertEquals(stat.getMax(), 70, 0.001);
		assertEquals(stat.getMin(), 10, 0.001);
		assertEquals(stat.getSum(), 120, 0.001);
		assertEquals(stat.getAvg(), 40, 0.001);		
	}
}
