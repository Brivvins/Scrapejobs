package com.briv.scrapejobs.controller.admin;

import com.briv.scrapejobs.service.scraper.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/v1/scrape")
@RequiredArgsConstructor
public class AdminController {

    private final ScraperService scraperService;

    @GetMapping("/run")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> runScraper() {
        scraperService.scrapeAllSources();
        return ResponseEntity.ok().build();
    }
}
