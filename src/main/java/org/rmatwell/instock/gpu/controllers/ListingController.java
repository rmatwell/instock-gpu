package org.rmatwell.instock.gpu.controllers;

import org.rmatwell.instock.gpu.services.ListingService;
import org.rmatwell.instock.gpu.domains.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Richard Atwell
 */
@RestController
@RequestMapping("/api")
public class ListingController {
    @Autowired
    private final ListingService service;

    ListingController(ListingService service){
        this.service = service;
    }

    @CrossOrigin(origins = {"http://localhost:3000"}) //local dev
    @GetMapping("/getListings")
    public List<Listing> getListings(){   return service.getListings();    }

    @PostMapping("/addListing")
    public void addListing(@RequestBody Listing listing){   service.add(listing);  }

    @PostMapping("/addListings")
    public void addListings(@RequestBody List<Listing> list){
        service.addListings(list);
    }

    @GetMapping("/getListings/{id}")
    public Listing getListingById(@PathVariable int id) throws Exception {  return service.getListingById(id);    }

    @GetMapping("/get-highest-price")
    public Listing findHighestPriceListing(){   return service.findFirstByOrderByPriceAsc();   }

    @GetMapping("/get-lowest-prices")
    public List<Object[]> findMinPricesByModel() {   return service.findMinPricesByModel();   }

}
