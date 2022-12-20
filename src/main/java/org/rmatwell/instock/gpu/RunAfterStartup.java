package org.rmatwell.instock.gpu;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.rmatwell.instock.gpu.domains.Listing;
import org.rmatwell.instock.gpu.services.BBListingService;
import org.rmatwell.instock.gpu.services.ListingService;
import org.rmatwell.instock.gpu.services.MCListingService;
import org.rmatwell.instock.gpu.utils.CSVReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.rmatwell.instock.gpu.utils.DateUtils.dateTodayToString;
import static org.rmatwell.instock.gpu.utils.DateUtils.dateFormat;
/**
 * @author Richard Atwell
 */
@Component
public class RunAfterStartup {

    private static final Logger log = LoggerFactory.getLogger(RunAfterStartup.class);
    String DATE = dateTodayToString();

    @Autowired
    ListingService listingService;

    public RunAfterStartup(ListingService listingService) {
        this.listingService = listingService;
    }

    //@Scheduled(cron = "1 4 21 * * *")
    @EventListener(ApplicationReadyEvent.class)
    public void runNow() throws IOException {
        log.info("Web-scraper started @ {}", dateFormat.format(new Date()));

        List<Listing> listings = new ArrayList<>();

        MCListingService mcListingService = new MCListingService();
        mcListingService.getListings(listings);

        BBListingService bbListingService = new BBListingService();
        bbListingService.getListings(listings);

        postJSONtoAPI(listings);

        CSVReport report = new CSVReport("LISTINGS_" + DATE + ".csv");
        report.writeListingsToCSV(listings);
        log.info("Web-scraper finished @ {}", dateFormat.format(new Date()));
    }

    private void postJSONtoAPI(List<Listing> listings){
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("LISTINGS_" +
                            DATE + ".json"), listings);

            String JSON = mapper.writeValueAsString(listings);

            listingService.addListings(listings);

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


//
//    private static final Logger log = LoggerFactory.getLogger(RunAfterStartup.class);
//
//    @Scheduled(cron = "1 4 21 * * *")
//    @EventListener(ApplicationReadyEvent.class)
//    @Autowired
//    public static void runAfterStartup(ListingService listingService) throws IOException {
//        App app = new App(listingService);
//        log.info("Web-scraper started @ {}", dateFormat.format(new Date()));
//        app.runNow();
//        log.info("Web-scraper finished @ {}", dateFormat.format(new Date()));
//    }
}