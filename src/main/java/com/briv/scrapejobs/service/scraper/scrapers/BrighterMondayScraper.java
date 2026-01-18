package com.briv.scrapejobs.service.scraper.scrapers;

import com.briv.scrapejobs.domain.job.Job;
import com.briv.scrapejobs.repository.job.JobRepository;
import com.briv.scrapejobs.service.scraper.JobScraper;
import com.briv.scrapejobs.service.scraper.ScrapeResult;
import com.briv.scrapejobs.util.parser.PostedDayParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BrighterMondayScraper implements JobScraper {

    private static final String SOURCE = "BrighterMonday";
    private static final int MAX_PAGE = 20;

    private final JobRepository jobRepository;

    @Override
    public String getSource() {
        return SOURCE;
    }

    @Override
    public ScrapeResult scrape() throws Exception {

        int page = 1;
        int totalFound = 0;
        int totalSaved = 0;
        boolean hadErrors = false;

        while (page <= MAX_PAGE) {

            String url = "https://www.brightermonday.co.ke/jobs?page=" + page;

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 )")
                    .timeout(30000)
                    .get();

            Elements jobCards = doc.select("div[data-cy=listing-cards-components]");

            if (jobCards.isEmpty()) {
                break;
            }

            totalFound += jobCards.size();

            for (Element card : jobCards) {
                try {
                    Job job = parseJob(card);
                    if (job == null) continue;

                    if (!jobRepository.existsBySourceUrl(job.getSourceUrl())) {
                        jobRepository.save(job);
                        totalSaved++;
                    }

                } catch (Exception e) {
                    hadErrors = true;
                }
            }

            page++;
        }

        return ScrapeResult.builder()
                .jobsFound(totalFound)
                .jobsSaved(totalSaved)
                .partialFailure(hadErrors)
                .build();
    }

    private Job parseJob(Element card) {

        Element titleLink = card.selectFirst("a[data-cy=listing-title-link]");
        if (titleLink == null) return null;

        String title = titleLink.text().trim();
        String jobUrl = titleLink.attr("abs:href");
        if (title.isEmpty() || jobUrl.isEmpty()) return null;

        String company = extractCompany(card);
        String location = "";
        String jobType = "";
        String salary = "";

        Element badgeContainer = card.selectFirst("div.flex.flex-wrap.mt-3");
        if (badgeContainer != null) {
            List<String> badges = badgeContainer.select("span").stream()
                    .map(e -> e.text().trim())
                    .filter(t -> !t.isEmpty())
                    .toList();

            if (!badges.isEmpty()) location = badges.get(0);
            if (badges.size() > 2) jobType = badges.get(1);
            if (badges.size() > 3) salary = badges.get(2);
        }

        String category = card.select("p.text-sm.text-gray-500.inline-block").text();
        String description = extractDescription(card);

        String postedDateText = Objects.requireNonNull(
                card.select("p.text-sm.font-normal.text-gray-700").first()
        ).text();

        return Job.builder()
                .title(title)
                .company(company)
                .location(location)
                .description(description)
                .source(SOURCE)
                .sourceUrl(jobUrl)
                .category(category)
                .jobType(jobType)
                .salaryRange(salary)
                .postedDate(PostedDayParser.parse(postedDateText))
                .createdAt(LocalDate.now())
                .build();
    }

    private String extractCompany(Element card) {
        Element link = card.selectFirst("p a[href^=/company]");
        if (link != null) return link.text().trim();

        Element fallback = card.selectFirst("p.text-sm");
        return fallback != null ? fallback.text().trim() : "";
    }

    private String extractDescription(Element card) {
        Element desc = card.selectFirst("div.flex.flex-col.border-t p.text-sm.font-normal.text-gray-700");
        return desc != null ? desc.text().trim() : "";
    }
}
