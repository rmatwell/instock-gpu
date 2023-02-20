package org.rmatwell.instock.gpu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Date;

import org.rmatwell.instock.gpu.service.ListingService;
import static org.rmatwell.instock.gpu.utils.DateUtils.dateFormat;

/**
 * @author Richard Atwell
 */
@Component
public class RunAfterStartup {
    private static final Logger log = LoggerFactory.getLogger(RunAfterStartup.class);
    @Autowired
    private final ListingService listingService;

    public RunAfterStartup(ListingService listingService) {
        this.listingService = listingService;
    }
    @Scheduled(cron = "1 4 1 * * *")
    @EventListener(ApplicationReadyEvent.class)
    public void runNow() throws IOException {
        log.info("Web-scraper started @ {}", dateFormat.format(new Date()));

        listingService.getMCListings();
        listingService.getBBListings();

        log.info("Web-scraper finished @ {}", dateFormat.format(new Date()));
    }

}
