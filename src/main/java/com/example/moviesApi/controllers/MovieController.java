package com.example.moviesApi.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.moviesApi.dto.MovieDto;
import com.example.moviesApi.services.MovieService;

@RestController
@RequestMapping("/v1/movie")
public class MovieController {

	private final MovieService movieService;

	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}
	
	/*--------------GET Movie By Id--------*/
	@GetMapping("/{id}")
	public ResponseEntity<?> getMovieById(@PathVariable Integer id) {
	    try {
	        // Call the service method to retrieve the movie by id.
	        MovieDto movieDto = movieService.getMovieById(id);
	        return ResponseEntity.ok(movieDto);
	    } catch (RuntimeException ex) {
	        // Log the error if a logger is available (e.g., logger.error("Error fetching movie", ex);)
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body( ex.getMessage());
	    }
	}
	
	/*--------------GET All Movies-------*/
	@GetMapping
	public ResponseEntity<?> getAllMovies() {
	    try {
	        // Call the service method to get all movies.
	        List<MovieDto> movieList = movieService.getAllMovies();
	        return ResponseEntity.ok(movieList);
	    } catch (RuntimeException ex) {
	        // Log the error if you have a logger (e.g., logger.error("Error fetching movies", ex);)
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred: " + ex.getMessage());
	    }
	}
    
	/*--------------UPDATE a Movie -----------------*/
	@PutMapping("/{id}")
	public ResponseEntity<MovieDto> updateMovie(
	        @PathVariable Integer id,
	        @RequestPart MovieDto movieDto,
	        @RequestPart(required = false) MultipartFile poster) {
	    try {
	        // Ensure the ID from the path is set into the DTO
	        movieDto.setMovieId(id);

	        MovieDto updatedMovie = movieService.updateMovie(movieDto, poster);
	        return ResponseEntity.ok(updatedMovie);
	    } catch (RuntimeException | IOException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	}

  /*-------------------CREATE a Movie-----------------*/
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createMovie(@RequestPart("movie") MovieDto movieDto,
			@RequestPart("poster") MultipartFile poster) throws IOException {
		try {
			MovieDto savedMovie = movieService.saveMovie(movieDto, poster);
			return ResponseEntity.ok(savedMovie);
		} catch (RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred: " + ex.getMessage());
		}
	}
}
