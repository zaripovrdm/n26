package com.n26.challenge.model;

public class SecondStatistics {
	
	long second;
	
	long count;
	double sum;
	double min;
	double max;
	
	public SecondStatistics() { }
		
	public SecondStatistics(long second, double amount) {
		this.second = second;
		this.count = 1;
		this.sum = amount;
		this.min = amount;
		this.max = amount;
	}

	@Override
	public String toString() {
		return "SecondStatistics [second=" + second + ", count=" + count + ", sum=" + sum + ", min=" + min + ", max=" + max + "]";
	}

	public long getSecond() {
		return second;
	}
	public void setSecond(long second) {
		this.second = second;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
}
