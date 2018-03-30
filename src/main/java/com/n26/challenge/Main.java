package com.n26.challenge;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import reactor.ipc.netty.http.server.HttpServer;

public class Main {
	
	public static void main(String... args) throws InterruptedException {
		
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class)) {	
			
			HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build(); // Creating handler around spring context			
			HttpServer.create(10000).newHandler(new ReactorHttpHandlerAdapter(handler)).block(); // Starting netty server						
			Thread.currentThread().join(); // Preventing application from exit
		}			
	}
}
