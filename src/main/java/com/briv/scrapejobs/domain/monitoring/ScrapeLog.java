package com.briv.scrapejobs.domain.monitoring;

import com.briv.scrapejobs.service.scraper.ScrapeResult;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scrape_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScrapeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;

    @Enumerated(EnumType.STRING)
    private ScrapeStatus status;

    @Column(name = "jobs_found")
    private Integer jobsFound;

    @Column(name = "jobs_saved")
    private Integer jobsSaved;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "response_time_ms")
    private Integer responseTimeMs;

    @Column(name = "error_message")
    private String errorMessage;

    //creation Factory
    public static ScrapeLog started(String source) {
        ScrapeLog log = new ScrapeLog();
        log.source = source;
        log.status = ScrapeStatus.RUNNING;
        log.startedAt = LocalDateTime.now();
        return log;
    }

    public void markSuccess(ScrapeResult result, int durationMs) {
        ensureRunning();

        this.status = result.isPartialFailure()
                ? ScrapeStatus.PARTIAL
                : ScrapeStatus.SUCCESS;

        this.jobsFound = result.getJobsFound();
        this.jobsSaved = result.getJobsSaved();
        completed(durationMs);
    }

    public void markFailure(Throwable t, int durationMs) {
        ensureRunning();
        this.status = ScrapeStatus.FAILED;
        this.errorMessage = safeMessage(t);
        completed(durationMs);
    }

    private void completed(int durationMs) {
        this.responseTimeMs = durationMs;
        this.completedAt = LocalDateTime.now();
    }

    private void ensureRunning() {
        if (this.status != ScrapeStatus.RUNNING) {
            throw new IllegalStateException("ScrapeLog already completed with status " + status);
        }
    }

    private String safeMessage(Throwable t) {
        if (t == null || t.getMessage() == null) {
            return "Unknown error";
        }
        return t.getMessage().length() > 500
                ? t.getMessage().substring(0, 500)
                : t.getMessage();
    }
}
