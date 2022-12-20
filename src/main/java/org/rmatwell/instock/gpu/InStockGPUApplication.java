package org.rmatwell.instock.gpu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rmatwell.instock.gpu.domains.Listing;
import org.rmatwell.instock.gpu.services.ListingService;
import org.rmatwell.instock.gpu.services.BBListingService;
import org.rmatwell.instock.gpu.utils.CSVReport;
import org.rmatwell.instock.gpu.services.MCListingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.rmatwell.instock.gpu.utils.DateUtils.dateTodayToString;

@SpringBootApplication
@EnableScheduling
public class InStockGPUApplication {

	final String DATE = dateTodayToString();

	public static void main(String[] args) {	SpringApplication.run(InStockGPUApplication.class, args);	}

//	@Bean
//	CommandLineRunner runner(ListingService listingService){
//		return args -> {
//			// read JSON and load json
//			List<Listing> listings = new ArrayList<>();
//
//			MCListingService mcListingService = new MCListingService();
//			mcListingService.getListings(listings);
//
//			BBListingService bbListingService = new BBListingService();
//			bbListingService.getListings(listings);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			try {
//				mapper.writerWithDefaultPrettyPrinter()
//						.writeValue(new File("LISTINGS_" +
//								DATE + ".json"), listings);
//
//				String JSON = mapper.writeValueAsString(listings);
//
//				listingService.addListings(listings);
//
//				CSVReport report = new CSVReport("LISTINGS_" + DATE + ".csv");
//				report.writeListingsToCSV(listings);
//			}catch (
//					IOException e) {
//				e.printStackTrace();
//			}
//		};



}
