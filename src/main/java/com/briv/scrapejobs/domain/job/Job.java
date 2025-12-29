package com.briv.scrapejobs.domain.job;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "jobs", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_company", columnList = "company"),
        @Index(name = "idx_location", columnList = "location"),
        @Index(name = "idx_job_type", columnList = "job_type"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_posted_date", columnList = "posted_date"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_source_url", columnList = "source_url", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String source;

    @Column(unique = true, name = "source_url")
    private String sourceUrl;

    private String category;
    private String jobType;
    private String salaryRange;

    @Column(name = "posted_date")
    private LocalDate postedDate;
//    private LocalDate expiresDate;

    @Column(updatable = false, name = "created_at")
    private LocalDate createdAt = LocalDate.now();

}
