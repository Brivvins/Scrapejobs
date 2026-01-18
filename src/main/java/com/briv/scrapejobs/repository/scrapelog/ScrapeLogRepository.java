package com.briv.scrapejobs.repository.scrapelog;

import com.briv.scrapejobs.domain.monitoring.ScrapeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapeLogRepository extends JpaRepository<ScrapeLog, Long> {

}
