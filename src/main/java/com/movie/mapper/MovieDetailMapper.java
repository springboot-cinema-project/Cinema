package com.movie.mapper;

import com.movie.domain.MovieDetails;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MovieDetailMapper {

    @Insert("INSERT INTO movie_details (movie_id, trailer, movie_img, country, production, distribution, director, actor, content) values (#{movieId}, #{trailer}, #{movieImg}, #{country}, #{production}, #{distribution}, #{director}, #{actor}, #{content})")
    public long insertMovieDetail(MovieDetails movieDetails);

    @Select("SELECT * FROM movie_details WHERE movie_id = #{movieId}")
    public MovieDetails getMovieDetail(long movieId);
}
