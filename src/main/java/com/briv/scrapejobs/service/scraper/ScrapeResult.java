package com.briv.scrapejobs.service.scraper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScrapeResult {
    private final int jobsFound;

    private final int jobsSaved;

    private final boolean partialFailure;
}
