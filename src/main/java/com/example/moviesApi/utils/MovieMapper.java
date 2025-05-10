package com.example.moviesApi.utils;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.moviesApi.dto.MovieDto;
import com.example.moviesApi.entities.Movie;


//MovieMapper.java
@Component

public class MovieMapper {
 
 

 public MovieDto toDto(Movie movie) {
     return new MovieDto(
             movie.getMovieId(),
             movie.getTitle(),
             movie.getDirector(),
             movie.getStudio(),
             movie.getMovieCast(),
             movie.getReleaseYear(),
             movie.getPosterUrl(),
             movie.getPosterName()
     );
 }

 public Movie toEntity(MovieDto dto, String posterName) {
     return new Movie(
             null,
             dto.getTitle(),
             dto.getDirector(),
             dto.getStudio(),
             dto.getMovieCast(),
             dto.getReleaseYear(),
             FileUtils.getPosterUrl(posterName),
             posterName
     );
 }

 public void updateEntity(Movie movie, MovieDto dto) {
     Optional.ofNullable(dto.getTitle()).ifPresent(movie::setTitle);
     Optional.ofNullable(dto.getDirector()).ifPresent(movie::setDirector);
     Optional.ofNullable(dto.getStudio()).ifPresent(movie::setStudio);
     Optional.ofNullable(dto.getMovieCast()).ifPresent(movie::setMovieCast);
     Optional.ofNullable(dto.getReleaseYear()).ifPresent(movie::setReleaseYear);
 }
}