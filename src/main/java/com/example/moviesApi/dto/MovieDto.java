package com.example.moviesApi.dto;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
	

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
	    private Set<@NotBlank(message = "Cast member name cannot be blank") String> movieCast;

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

}
