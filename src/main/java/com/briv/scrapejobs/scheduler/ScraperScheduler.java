package com.briv.scrapejobs.scheduler;

import com.briv.scrapejobs.service.scraper.ScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScraperScheduler {

    private final ScraperService scraperService;

    @Scheduled(cron = "0 0 6 * * *")
    public void scrape() {
       try{
           log.info("Daily Scheduled Scraping started...");
           scraperService.scrapeAllSources();
           log.info("Daily Scheduled Scraping finished");
       }catch(Exception e){
           log.error("Scheduled Scraping failed : {}", e.getMessage(), e);
       }
    }

}
