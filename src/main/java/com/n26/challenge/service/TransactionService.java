package com.n26.challenge.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.n26.challenge.model.SecondStatistics;
import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;

@Service
public class TransactionService {
	
	private final List<SecondStatistics> storage;
	private final Comparator<SecondStatistics> comparator = Comparator.comparingLong(SecondStatistics::getSecond).reversed();
	
	private static final int SECOND_COUNT = 60;
	
	TransactionService(List<SecondStatistics> storage) { // For tests
		this.storage = storage;
	}
	
	public TransactionService() {
		this(new ArrayList<>());
	}
	
	public boolean putTransaction(Transaction transaction) {
		
		if (transaction.getTimestamp() < System.currentTimeMillis() - SECOND_COUNT * 1000) {
			// Nothing happens if we receive transaction older than 60 seconds
			return false;
		}
		
		long second = transaction.getTimestamp() / 1000;
		
		synchronized (storage) {
			// Trying to find statistics for second from incoming transaction
			SecondStatistics statistics = storage.stream()
				.filter(stat -> stat.getSecond() == second)
				.findFirst()
				.orElse(null);
			
			if (statistics != null) { // We already have some statistics for that second, so just need to update it
				statistics.setSum(statistics.getSum() + transaction.getAmount());		
				statistics.setMax(Math.max(statistics.getMax(), transaction.getAmount()));
				statistics.setMin(Math.min(statistics.getMin(), transaction.getAmount()));
				statistics.setCount(statistics.getCount() + 1);			
			} else { // Creating new entry in storage
				storage.add(new SecondStatistics(second, transaction.getAmount()));			
				storage.sort(comparator); // Sorting entries in desc order to have quick access to last 60 elements while computing statistics for 60 seconds
				
				if (storage.size() > SECOND_COUNT) { // We need to keep maximum 60 elements, oldest entries should be deleted to save memory				
					storage.subList(SECOND_COUNT, storage.size()).clear();				
				}			
			}
		}
		
		return true;
	}
	
	// Doesn't matter how many transactions we received during last minute. We always iterate here over maximum 60 entries creating the same objects and performing the same operations.
	// So time and memory complexity is close to be constant.
	public Statistics getStatistics() {
		Statistics statistics = new Statistics();
		
		long threshold = System.currentTimeMillis() / 1000 - SECOND_COUNT;
		
		synchronized (storage) {
			for (SecondStatistics stat : storage) { 
				if (stat.getSecond() <= threshold) { // entries older than 60 second will not be taken into account
					break;
				}
				
				statistics.setSum(statistics.getSum() + stat.getSum());		
				statistics.setMax(Math.max(statistics.getMax(), stat.getMax()));
				statistics.setCount(statistics.getCount() + stat.getCount());
				
				if (statistics.getMin() == 0) { // Workaroud , because 0 is default min value and lowerest possible at the same time
					statistics.setMin(stat.getMin());
				} else {
					statistics.setMin(Math.min(statistics.getMin(), stat.getMin()));
				}
			}
		}
		
		if (statistics.getCount() > 0) {
			statistics.setAvg(statistics.getSum() / statistics.getCount());
		}
			
		return statistics;
	}
	
	List<SecondStatistics> getStorage() { // Fot tests
		return storage;
	}
}
