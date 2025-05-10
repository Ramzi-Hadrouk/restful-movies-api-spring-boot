package com.example.moviesApi.dto;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MovieDto {

    private Integer movieId;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Director name is required")
    @Size(max = 50, message = "Director name cannot exceed 50 characters")
    private String director;

    @NotBlank(message = "Studio name is required")
    @Size(max = 50, message = "Studio name cannot exceed 50 characters")
    private String studio;

    @NotEmpty(message = "At least one cast member is required")
    private Set<String> movieCast;

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be 1888 or later")
    @Max(value = 2100, message = "Release year must be 2100 or earlier")
    private Integer releaseYear;

    @NotBlank(message = "Poster URL is required")
    @Pattern(regexp = "^(https?://).+", message = "Poster URL must start with http:// or https://")
    private String posterUrl;

    @NotBlank(message = "posterName is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String posterName;





	//--------------------	// Constructor with all fields and setters/getters  --------------------//
    // Default constructor
    public MovieDto() {
    }

    // All-args constructor
    public MovieDto(Integer movieId, String title, String director, String studio, Set<String> movieCast, Integer releaseYear, String posterUrl, String posterName) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.studio = studio;
        this.movieCast = movieCast;
        this.releaseYear = releaseYear;
        this.posterUrl = posterUrl;
        this.posterName = posterName;
    }

    // Getters and Setters
    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public Set<String> getMovieCast() {
        return movieCast;
    }

    public void setMovieCast(Set<String> movieCast) {
        this.movieCast = movieCast;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }
}
