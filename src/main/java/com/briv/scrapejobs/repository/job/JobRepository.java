package com.briv.scrapejobs.repository.job;

import com.briv.scrapejobs.model.job.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Override
    Page<Job> findAll(Pageable pageable);
    boolean existsBySourceUrl(String sourceUrl);
}
