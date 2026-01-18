package com.briv.scrapejobs.util.monitoring;

import com.briv.scrapejobs.domain.monitoring.ScrapeLog;
import com.briv.scrapejobs.exception.ScrapingFailedException;
import com.briv.scrapejobs.repository.scrapelog.ScrapeLogRepository;
import com.briv.scrapejobs.service.scraper.JobScraper;
import com.briv.scrapejobs.service.scraper.ScrapeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScrapeMonitor {

    private final ScrapeLogRepository scrapeLogRepository;

    @Transactional
    public ScrapeResult run(JobScraper scraper) {
        String source = scraper.getSource();
        ScrapeLog log = ScrapeLog.started(source);
        scrapeLogRepository.save(log);

        long start = System.currentTimeMillis();

        try {
            ScrapeResult result = scraper.scrape();
            int durationMs = (int) (System.currentTimeMillis() - start);
            log.markSuccess(result, durationMs);
            return result;

        } catch (Exception e) {
            int durationMs = (int) (System.currentTimeMillis() - start);
            log.markFailure(e, durationMs);
            throw new ScrapingFailedException(e);
        }
    }
}
