package com.n26.challenge;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.n26.challenge.controller.TransactionController;
import com.n26.challenge.service.TransactionService;

@Configuration
@EnableWebFlux
@ComponentScan(basePackageClasses = {TransactionController.class, TransactionService.class})
public class AppConfiguration implements WebFluxConfigurer { }
