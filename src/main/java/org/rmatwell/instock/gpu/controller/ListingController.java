package org.rmatwell.instock.gpu.controller;

import org.rmatwell.instock.gpu.service.ListingService;
import org.rmatwell.instock.gpu.domain.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * @author Richard Atwell
 */
@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {
    @Autowired
    private final ListingService listingService;
    ListingController(ListingService listingService){
        this.listingService = listingService;
    }

    @GetMapping("/")
    public List<Listing> getListings(){   return listingService.getListings();    }

    @GetMapping("/{id}")
    public Listing getListingById(@PathVariable int id) throws Exception {  return listingService.getListingById(id);    }

    @GetMapping("/current-date")
    public List<Listing> getListingsForToday() { return listingService.getListingsByMostRecentDate();}

    @GetMapping("/models/{model}")
    public List<Listing> findListingsByModel(@PathVariable String model) throws Exception {
        return listingService.getListingsByModel(model);
    }

    @GetMapping("/highest-price")
    public Listing findHighestPriceListing(){   return listingService.findFirstByOrderByPriceAsc();   }

    @GetMapping("/lowest-prices")
    public List<Object[]> findMinPricesByModel() {   return listingService.findMinPricesByModel();   }

    @GetMapping("/download/csv")
    public ResponseEntity<Resource> getFile() {
        String filename = "download.csv";
        InputStreamResource file = new InputStreamResource(listingService.createCSVFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
