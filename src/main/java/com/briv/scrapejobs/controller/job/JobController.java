package com.briv.scrapejobs.controller.job;

import com.briv.scrapejobs.dto.job.JobSearchRequest;
import com.briv.scrapejobs.domain.job.Job;
import com.briv.scrapejobs.repository.job.JobRepository;
import com.briv.scrapejobs.service.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobRepository jobRepository;
    private final JobService jobService;

    @GetMapping("/search")
    public ResponseEntity<Page<Job>> searchJobs(@ModelAttribute JobSearchRequest req) {
        return jobService.searchJobs(req);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
