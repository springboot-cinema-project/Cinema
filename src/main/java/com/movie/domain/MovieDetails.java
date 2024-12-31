package com.movie.domain;

import lombok.Data;

@Data
public class MovieDetails {

    private Long id;
    private Long movieId;
    private String trailer;
    private String movieImg;
}
