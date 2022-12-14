package org.rmatwell.instock.gpu;


import org.rmatwell.webscraper.App;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Richard Atwell
 */
@Component
public class RunAfterStartup {

    private static final Logger log = LoggerFactory.getLogger(RunAfterStartup.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 0/10 * * * *")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

////    @Scheduled(cron = "1 4 21 * * *")
    @EventListener(ApplicationReadyEvent.class)
    public static void runAfterStartup() throws IOException {
        System.out.println("Ran After Start");
        App app = new App();
        System.out.println("Web-scraper started @ " + dateFormat.format(new Date()));
        app.runNow();
        System.out.println("Web-scraper finished @ " + dateFormat.format(new Date()));
    }

}


