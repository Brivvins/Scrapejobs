package com.briv.scrapejobs.model.job;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String Company;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String Source;

    @Column(unique = true)
    private String sourceUrl;

    private String category;
    private String jobType;
    private String salaryRange;

    private LocalDate postedDate;
    private LocalDate expiresDate;

    @Column(updatable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();
}
