package com.example.moviesApi.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.moviesApi.dto.MovieDto;
import com.example.moviesApi.entities.Movie;
import com.example.moviesApi.exceptions.FileUploadException;
import com.example.moviesApi.exceptions.ResourceNotFoundException;
import com.example.moviesApi.exceptions.ValidationException;
import com.example.moviesApi.repositories.MovieRepository;
import com.example.moviesApi.utils.FileUtils;
import com.example.moviesApi.utils.MovieMapper;

 

@Service
public class MovieService {
 
 private final MovieRepository movieRepository;
 private final PosterService posterService;
 private final MovieMapper movieMapper;

 public MovieService(MovieRepository movieRepository, 
                   PosterService posterService,
                   MovieMapper movieMapper) {
     this.movieRepository = movieRepository;
     this.posterService = posterService;
     this.movieMapper = movieMapper;
 }

 public MovieDto getMovieById(Integer id) {
     return movieRepository.findById(id)
             .map(movieMapper::toDto)
             .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
 }

 public List<MovieDto> getAllMovies() {
     return movieRepository.findAll().stream()
             .map(movieMapper::toDto)
             .collect(Collectors.toList());
 }

 public MovieDto saveMovie(MovieDto movieDto, MultipartFile poster) throws IOException {
	    validatePoster(poster);
	    
	    String posterName = posterService.uploadPoster(poster);
	    if (posterName == null || posterName.isEmpty()) {
	        throw new FileUploadException("Failed to upload movie poster");
	    }
	    System.out.println(" \n EVERY THING OK UNTEL HERE \n");
	    Movie movie = movieMapper.toEntity(movieDto, posterName);
	    return movieMapper.toDto(movieRepository.save(movie));
	}

 public MovieDto updateMovie(MovieDto movieDto, MultipartFile poster) throws IOException {
     Movie existingMovie = movieRepository.findById(movieDto.getMovieId())
             .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieDto.getMovieId()));

     movieMapper.updateEntity(existingMovie, movieDto);
     
     if (poster != null && !poster.isEmpty()) {
         validatePoster(poster);
         String posterName = posterService.uploadPoster(poster);
         if (posterName == null || posterName.isEmpty()) {
             throw new FileUploadException("Failed to update movie poster");
         }
         existingMovie.setPosterName(posterName);
         existingMovie.setPosterUrl(FileUtils.getPosterUrl(posterName));
     }

     return movieMapper.toDto(movieRepository.save(existingMovie));
 }

 public void deleteMovie(Integer id) {
     Movie movie = movieRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
     movieRepository.delete(movie);
 }

 private void validatePoster(MultipartFile poster) {
     if (poster == null || poster.isEmpty()) {
         throw new ValidationException("Poster file is required");
     }
     if (!poster.getContentType().startsWith("image/")) {
         throw new ValidationException("Invalid file type for poster");
     }
 }
}