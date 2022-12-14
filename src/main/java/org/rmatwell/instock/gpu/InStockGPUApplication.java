package org.rmatwell.instock.gpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InStockGPUApplication {

	public static void main(String[] args) {	SpringApplication.run(InStockGPUApplication.class, args);	}

}
