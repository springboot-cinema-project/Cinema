package com.movie.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Movies {

    private Long id;
    private String title;
    private Long runningTime;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private Long audience;
    private String country;
    private String production;
    private String distribution;
    private String genre;
    private String director;
    private String actor;
    private Double book_rate;
    private String posterImg;
    private String content;

}
