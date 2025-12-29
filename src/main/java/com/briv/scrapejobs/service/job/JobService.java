package com.briv.scrapejobs.service.job;

import com.briv.scrapejobs.dto.job.JobSearchRequest;
import com.briv.scrapejobs.domain.job.Job;
import com.briv.scrapejobs.repository.job.JobRepository;
import com.briv.scrapejobs.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;

    public ResponseEntity searchJobs(JobSearchRequest req) {

        log.info("Searching jobs: keyword: {}, Location: {}, Job-Type: {}, Category: {}",
                req.getKeyword(), req.getLocation(), req.getJobType(), req.getCategory());

        Specification<Job> spec = Specification.allOf(
                JobSpecification.hasKeyword(req.getKeyword()),
                JobSpecification.hasLocation(req.getLocation()),
                JobSpecification.hasJobType(req.getJobType()),
                JobSpecification.hasCategory(req.getCategory()),
                JobSpecification.hasCompany(req.getCompany()),
                JobSpecification.postedAfter(req.getPostedDate())
        );

        int page = req.getPage();
        int pageSize = req.getPageSize();

//        Sort sort = req.getSortDir().equalsIgnoreCase("desc")
//                ? Sort.by(req.getSortBy()).descending()
//                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(page, pageSize);
        Page jobsPage = jobRepository.findAll(spec,pageable);

        log.info("Found {} jobs found (page {} of {})",
                jobsPage.getTotalElements(),
                jobsPage.getNumber() + 1,
                jobsPage.getTotalElements());

        return ResponseEntity.ok(jobsPage);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).
                orElseThrow(
                        () -> new RuntimeException("Job Not Found with id: " + id)
        );
    }
}