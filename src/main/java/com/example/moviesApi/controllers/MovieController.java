package com.example.moviesApi.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * Endpoint to create a new movie.
     * <p>
     * Accepts a multipart/form-data request containing:
     * <ul>
     *   <li><strong>movie:</strong> a JSON representation of MovieDto</li>
     *   <li><strong>poster:</strong> a MultipartFile representing the poster image</li>
     * </ul>
     * </p>
     *
     * @param movieDto the movie details
     * @param poster   the movie poster file
     * @return the saved MovieDto as a JSON response or an error message.
     * @throws IOException 
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovie(
            @RequestPart("movie") MovieDto movieDto,
            @RequestPart("poster") MultipartFile poster) throws IOException {
        try {
            MovieDto savedMovie = movieService.saveMovie(movieDto, poster);
            return ResponseEntity.ok(savedMovie);
        } catch (RuntimeException ex) {
            // Log the error for debugging (if you have a logger, e.g., Logger logger = LoggerFactory.getLogger(MovieController.class))
            // logger.error("Error saving movie: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred: " + ex.getMessage());
        }
    }
}
