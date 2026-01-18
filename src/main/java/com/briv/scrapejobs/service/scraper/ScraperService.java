package com.briv.scrapejobs.service.scraper;

import com.briv.scrapejobs.util.monitoring.ScrapeMonitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScraperService {

    private final ScrapeMonitor scrapeMonitor;
    private final List<JobScraper> scrapers;

    public void scrapeAllSources() {
        log.info("Starting job scraping for {} sources", scrapers.size());

        for (JobScraper scraper : scrapers) {
            scrapeMonitor.run(scraper);
        }

        log.info(" Finished job scraping for all sources");
    }


}
