package com.briv.scrapejobs.repository.job;

import com.briv.scrapejobs.domain.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

//    Page<Job> findAllPaged(Pageable pageable);
    boolean existsBySourceUrl(String sourceUrl);
}
