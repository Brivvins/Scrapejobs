package com.briv.scrapejobs.dto.job;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobSearchRequest {

    private String keyword;
    private String location;
    private String jobType;
    private String category;
    private String company;
    private LocalDate postedDate;
    private Integer page = 0;
    private Integer pageSize = 10;

    private String sortBy = "postedDate";
    private String sortDir = "desc";
}
