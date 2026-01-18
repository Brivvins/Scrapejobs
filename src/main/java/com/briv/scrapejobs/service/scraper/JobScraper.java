package com.briv.scrapejobs.service.scraper;

public interface JobScraper {

    String getSource();

    ScrapeResult scrape() throws Exception;
}
