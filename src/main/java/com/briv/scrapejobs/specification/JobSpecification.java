package com.briv.scrapejobs.specification;

import com.briv.scrapejobs.domain.job.Job;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("company")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    public static Specification<Job> hasLocation(String location) {
        return (root, query, cb) -> {
            if (location == null || location.isBlank()){
               return cb.conjunction();
            }

            String pattern = "%" + location.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("location")), pattern);
        };
    }

    public static Specification<Job> hasJobType(String jobType){
        return (root, query, cb) -> {
            if(jobType == null || jobType.isBlank()){
                return cb.conjunction();
            }

            return cb.equal(cb.lower(root.get("jobType")), jobType.toLowerCase());
        };
    }

    public static Specification<Job> hasCategory(String category){
        return (root, query, cb) -> {
            if(category == null || category.isBlank()){
                return cb.conjunction();
            }

            String pattern = "%" + category.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("category")), pattern);
        };
    }

    public static Specification<Job> hasCompany(String company) {
        return (root, query, cb) -> {
            if (company == null || company.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + company.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("company")), pattern);
        };
    }

    public static Specification<Job> postedAfter(java.time.LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("postedDate"), date);
        };
    }
}
