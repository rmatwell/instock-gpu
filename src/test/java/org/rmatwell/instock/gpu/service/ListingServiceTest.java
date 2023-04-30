package org.rmatwell.instock.gpu.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmatwell.instock.gpu.controller.ListingController;
import org.rmatwell.instock.gpu.domain.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListingController.class)
class ListingServiceTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListingService listingService;


    @Test
    public void shouldReturnJSONFromService() throws Exception{
        List<Listing> list = new ArrayList<>();
        when(listingService.getListings()).thenReturn(list);
        this.mockMvc.perform(get("/api/listings/get-listings")).andDo(print()).andExpect(status().isOk());
    }


    @Test
    void getListings() {
    }

    @Test
    void getListingsByMostRecentDate() {
    }

    @Test
    void getListingById() {
    }

    @Test
    void findFirstByOrderByPriceAsc() {
    }

    @Test
    void findMinPricesByModel() {
    }

    @Test
    void getMCListings() {
    }

    @Test
    void scrapeDataFromElements() {
    }

    @Test
    void getBBListings() {
    }

    @Test
    void createCSVFile() {
    }
}