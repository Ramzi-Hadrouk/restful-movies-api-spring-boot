package com.example.moviesApi.controllers;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.moviesApi.dto.MovieDto;
import com.example.moviesApi.exceptions.FileUploadException;
import com.example.moviesApi.exceptions.ResourceNotFoundException;
import com.example.moviesApi.exceptions.ValidationException;
import com.example.moviesApi.services.MovieService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
//MovieController.java
@RestController
@RequestMapping("/v1/movie")
@Validated
public class MovieController {
 
 private static final Logger logger = LoggerFactory.getLogger(MovieController.class);
 private final MovieService movieService;

 public MovieController(MovieService movieService) {
     this.movieService = movieService;
 }
 
 //-----------CREATE Movie -----------//
 @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 public ResponseEntity<MovieDto> createMovie(
         @RequestPart("movie")  MovieDto movieDto,
         @RequestPart("poster")  MultipartFile poster) throws IOException {
     
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(movieService.saveMovie(movieDto, poster));
 }

 //-------------------GET a Movie ById---------//
 @GetMapping("/{id}")
 public ResponseEntity<MovieDto> getMovieById(@PathVariable @Positive Integer id) {
     return ResponseEntity.ok(movieService.getMovieById(id));
 }

 @GetMapping
 public ResponseEntity<List<MovieDto>> getAllMovies() {
     return ResponseEntity.ok(movieService.getAllMovies());
 }
 
 //------------------UPDATE a Movie----------------//
 @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 public ResponseEntity<MovieDto> updateMovie(
         @PathVariable   Integer id,
         @RequestPart  MovieDto movieDto,
         @RequestPart(required = false) MultipartFile poster) throws IOException {
     
     movieDto.setMovieId(id);
     return ResponseEntity.ok(movieService.updateMovie(movieDto, poster));
 }

 
 //-----------------DELETE a Movie---------------//
 @DeleteMapping("/{id}")
 public ResponseEntity<Void> deleteMovie(@PathVariable @Positive Integer id) {
     movieService.deleteMovie(id);
     return ResponseEntity.noContent().build();
 }



 @ExceptionHandler({ResourceNotFoundException.class})
 public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
     return ResponseEntity.status(HttpStatus.NOT_FOUND)
             .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
 }

 @ExceptionHandler({FileUploadException.class, ValidationException.class})
 public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
             .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
 }

 @ExceptionHandler(Exception.class)
 public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
     logger.error("Unexpected error occurred", ex);
     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                     "An unexpected error occurred"));
 }

 public static class ErrorResponse {
     private final int status;
     private final String message;
     private final Instant timestamp;

     public ErrorResponse(int status, String message) {
         this.status = status;
         this.message = message;
         this.timestamp = Instant.now();
     }

     public int getStatus() { return status; }
     public String getMessage() { return message; }
     public Instant getTimestamp() { return timestamp; }
 }
}