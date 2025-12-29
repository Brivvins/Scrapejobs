package com.briv.scrapejobs.service.scraper;

import com.briv.scrapejobs.domain.job.Job;
import com.briv.scrapejobs.repository.job.JobRepository;
import com.briv.scrapejobs.util.parser.PostedDayParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScraperService {

    private final JobRepository jobRepository;

    public void scrapeAllSources() {
        log.info("Starting job scraping...");
        scrapeBrighterMonday();
        log.info("Job scraping completed");
    }

    private void scrapeBrighterMonday() {
        int page = 1;
        int MAX_PAGE = 20;
        int totalCount = 0;

        while (page <= MAX_PAGE) {
            String url = "https://www.brightermonday.co.ke/jobs?page=" + page;
            log.info("📡 Scraping BrighterMonday page {}", page);

            int savedCount = 0;

            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(15000)
                        .get();

                Elements jobCards = doc.select("div[data-cy=listing-cards-components]");
                log.info("📊 Found {} job listings", jobCards.size());

                if (jobCards.isEmpty()) {
                    log.info("No jobs found, stopping pagination...");
                    break;
                }

                for (Element card : jobCards) {
                    try {
                        Element titleLink = card.selectFirst("a[data-cy=listing-title-link]");
                        if (titleLink == null) continue;

                        String title = titleLink.text().trim();
                        if (title.isEmpty()) {
                            Element p = titleLink.selectFirst("p");
                            if (p != null) title = p.text().trim();
                        }

                        String jobUrl = titleLink.attr("abs:href");
                        if (title.isEmpty() || jobUrl.isEmpty()) continue;

                        String company = "";
                        Element companyLink = card.selectFirst("p a[href^=/company]");
                        if (companyLink != null) {
                            company = companyLink.text().trim();
                        }

                        if (company.isEmpty()) {
                            Element companyP = card.selectFirst("p.text-sm");
                            if (companyP != null) {
                                company = companyP.text().trim();
                            }
                        }

                        String location = "";
                        String jobType = "";
                        String salary = "";

                        Element badgeContainer = card.selectFirst("div.flex.flex-wrap.mt-3");
                        if (badgeContainer != null) {
                            Elements badges = badgeContainer.select("span");

                            List<String> badgeTexts = badges.stream()
                                    .map(e -> e.text().trim())
                                    .filter(t -> !t.isEmpty())
                                    .toList();

                            if (!badgeTexts.isEmpty()) location = badgeTexts.get(0);
                            if (badgeTexts.size() >= 2) jobType = badgeTexts.get(1);
                            if (badgeTexts.size() >= 3) salary = badgeTexts.get(2);
                        }

                        String category = card.select("p.text-sm.text-gray-500.inline-block").text();

                        String description = "";
                        Element descDiv = card.selectFirst("div.flex.flex-col.border-t p.text-sm.font-normal.text-gray-700");
                        if (descDiv != null) {
                            description = descDiv.text().trim();
                        }

                        String stringPostedDate = Objects.requireNonNull(card.select("p.text-sm.font-normal.text-gray-700").first()).text();
                        LocalDate postedDate = PostedDayParser.parse(stringPostedDate);

                        if (jobRepository.existsBySourceUrl(jobUrl)) {
                            log.debug("Skipping duplicate...: {}", title);
                            continue;
                        }

                        Job job = Job.builder()
                                .title(title)
                                .company(company)
                                .location(location)
                                .description(description)
                                .source("Brighter Monday")
                                .sourceUrl(jobUrl)
                                .category(category)
                                .jobType(jobType)
                                .salaryRange(salary)
                                .postedDate(postedDate)
                                .createdAt(LocalDate.now())
                                .build();

                        jobRepository.save(job);
                        savedCount++;

                        log.info("Saved: {} at {}", title, company.isEmpty() ? "Unknown" : company);

                    } catch (Exception e) {
                        log.error("❌ Error parsing job card: {}", e.getMessage());
                    }
                }

                page++;
                totalCount += savedCount;

                log.info("page {} saved {} jobs ", page, savedCount);
            } catch (IOException e) {
                log.error("❌ Error connecting to BrighterMonday: {}", e.getMessage());
            }
        }
        log.info("Scraping completed for Brighter Monday " +
                "saved {} jobs", totalCount);
    }
}
