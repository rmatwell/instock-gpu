package org.rmatwell.instock.gpu;

import org.rmatwell.webscraper.App;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.rmatwell.webscraper.utils.DateUtils.dateFormat;

/**
 * @author Richard Atwell
 */
@Component
public class RunAfterStartup {

    private static final Logger log = LoggerFactory.getLogger(RunAfterStartup.class);

//    @Scheduled(cron = "1 4 21 * * *")
    @EventListener(ApplicationReadyEvent.class)
    public static void runAfterStartup() throws IOException {
        App app = new App();
        log.info("Web-scraper started @ {}", dateFormat.format(new Date()));
        app.runNow();
        log.info("Web-scraper finished @ {}", dateFormat.format(new Date()));
    }
}