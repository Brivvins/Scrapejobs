package com.briv.scrapejobs.controller.scraper;

import com.briv.scrapejobs.service.scraper.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scraper")
@RequiredArgsConstructor
public class ScraperController {
    private final ScraperService scraperService;

    @GetMapping("/run")
    public ResponseEntity<?> runScraper() {
        scraperService.scrapeAllSources();
        return ResponseEntity.ok().build();
    }
}
