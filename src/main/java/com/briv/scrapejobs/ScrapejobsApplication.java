package com.briv.scrapejobs;

import com.briv.scrapejobs.model.job.Job;
import com.briv.scrapejobs.repository.job.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ScrapejobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapejobsApplication.class, args);
    }

    @Bean
    CommandLineRunner testData(JobRepository jobRepository) {
        return args -> {
            // Create test jobs
            Job job1 = new Job();
            job1.setTitle("Backend Developer");
            job1.setCompany("Tech Co Kenya");
            job1.setLocation("Nairobi");
            job1.setDescription("Looking for Spring Boot developer");
            job1.setSource("Manual");
            job1.setSourceUrl("http://test.com/job1");
            job1.setJobType("Full-time");
            job1.setPostedDate(LocalDate.now());

            Job job2 = new Job();
            job2.setTitle("Java Developer");
            job2.setCompany("Startup Ltd");
            job2.setLocation("Remote");
            job2.setDescription("Remote Java position");
            job2.setSource("Manual");
            job2.setSourceUrl("http://test.com/job2");
            job2.setJobType("Remote");
            job2.setPostedDate(LocalDate.now());

            jobRepository.save(job1);
            jobRepository.save(job2);

            System.out.println("✅ Test jobs created! Total: " + jobRepository.count());
        };
    }
}
