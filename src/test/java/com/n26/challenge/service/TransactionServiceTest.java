package com.n26.challenge.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.n26.challenge.model.SecondStatistics;
import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;

@Test
public class TransactionServiceTest {
	
	@Test
	public void testPutTransaction() {
		TransactionService service = new TransactionService();
		
		long timestamp = System.currentTimeMillis();
		
		assertTrue(service.putTransaction(new Transaction(70, timestamp - 1000 * 40))); // 1
		assertTrue(service.putTransaction(new Transaction(20, timestamp - 1000 * 20))); // 2
		assertFalse(service.putTransaction(new Transaction(45, timestamp - 1000 * 70))); // 3	
		assertTrue(service.putTransaction(new Transaction(40, timestamp - 1000 * 20))); // 4
				
		// Check storage content
		assertEquals(service.getStorage().size(), 2);
				
		// Result of invocations 2 and 4 should form single SecondStatistics instance. Following code tests it:
		SecondStatistics stat = service.getStorage().get(0);
		assertEquals(stat.getCount(), 2);
		assertEquals(stat.getMax(), 40, 0.001);
		assertEquals(stat.getMin(), 20, 0.001);
		assertEquals(stat.getSum(), 60, 0.001);
	}
	
	@Test
	public void testGetStatistics() {
		
		long timestamp = System.currentTimeMillis();
		
		List<SecondStatistics> storage = new ArrayList<>();
		
		storage.add(new SecondStatistics((timestamp - 20 * 1000) / 1000, 40));
		storage.add(new SecondStatistics((timestamp - 40 * 1000) / 1000, 80));
		storage.add(new SecondStatistics((timestamp - 70 * 1000) / 1000, 80)); // will not be taken into account
		
		TransactionService service = new TransactionService(storage);
		
		service.getStorage().stream().forEach(System.out::println);
		
		Statistics stat = service.getStatistics();
		
		assertEquals(stat.getCount(), 2);
		assertEquals(stat.getMax(), 80, 0.001);
		assertEquals(stat.getMin(), 40, 0.001);
		assertEquals(stat.getAvg(), 60, 0.001);
		assertEquals(stat.getSum(), 120, 0.001);
	}
}

