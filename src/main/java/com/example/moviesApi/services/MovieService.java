package com.example.moviesApi.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.moviesApi.dto.MovieDto;
import com.example.moviesApi.entities.Movie;
import com.example.moviesApi.repositories.MovieRepository;
import com.example.moviesApi.utils.FileUtils;

@Service
public class MovieService {
	private MovieRepository movieRepository;
	private PosterService posterService;

	public MovieService(MovieRepository movieRepo,PosterService posterService) {
		this.movieRepository = movieRepo;
		this.posterService = posterService;
	}
	
	/*----------------GET Movie By ID----------------*/
	public MovieDto getMovieById(Integer id) {
	    Movie movie = movieRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
	    return mapToMovieDto(movie);
	}
	
	/*----------------GET All Movies---------------*/
	public List<MovieDto> getAllMovies() {
	    // Retrieve all movies from the repository.
	    List<Movie> movies = movieRepository.findAll();
	    
	    // Map each Movie entity to a MovieDto.
	    return movies.stream()
	                 .map(this::mapToMovieDto)
	                 .collect(Collectors.toList());
	}

	/*----------------SAVE a Movie---------------*/
	public MovieDto saveMovie(MovieDto movieDto, MultipartFile poster) throws IOException {
		// Upload the poster and validate the result.
		String posterName = posterService.uploadPoster(poster);
		if (posterName == null || posterName.isEmpty()) {
			throw new RuntimeException("Failed to upload the movie poster.");
		}
		String posterUrl = FileUtils.getPosterUrl(posterName);
		Movie movie = buildMovie(movieDto, posterName, posterUrl);
		 movieRepository.save(movie);
       
		return mapToMovieDto(movie);
	}
   /*----------------UPDATE a Movie--------------*/
	public MovieDto updateMovie(MovieDto movieDto, MultipartFile poster) throws IOException {
	    // 1. Fetch the existing movie
	    Movie movie = movieRepository.findById(movieDto.getMovieId())
	            .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieDto.getMovieId()));

	    // 2. Update fields if they are not null
	     updateMovieFromDto(movieDto, movie);


	    // 3. Handle poster upload if a new poster is provided
	    if (poster != null && !poster.isEmpty()) {
	        String posterName = posterService.uploadPoster(poster);
	        if (posterName == null || posterName.isEmpty()) {
	            throw new RuntimeException("Failed to upload the movie poster.");
	        }
	        String posterUrl = FileUtils.getPosterUrl(posterName);
	        movie.setPosterName(posterName);
	        movie.setPosterUrl(posterUrl);
	    }

	    // 4. Save the updated movie
	    movieRepository.save(movie);

	    // 5. Return the updated DTO
	    return mapToMovieDto(movie);
	}

	
	
	
	
	
	
	//===============HELPERS FUNCTION================
	
	/**
	 * Helper method to Updaet  a Movie entity from a MovieDto.
	 */
	public void updateMovieFromDto(MovieDto dto, Movie movie) {
	    if (dto.getTitle() != null) movie.setTitle(dto.getTitle());
	    if (dto.getDirector() != null) movie.setDirector(dto.getDirector());
	    if (dto.getStudio() != null) movie.setStudio(dto.getStudio());
	    if (dto.getMovieCast() != null) movie.setMovieCast(dto.getMovieCast());
	    if (dto.getReleaseYear() != null) movie.setReleaseYear(dto.getReleaseYear());
	    if (dto.getPosterName() != null) movie.setPosterName(dto.getPosterName());
	}

	
	/**
	 * Helper method to build a Movie entity from a MovieDto.
	 */
	private Movie buildMovie(MovieDto dto, String posterName, String posterUrl) {
		// The movieId is null here as it is generated automatically.
		return new Movie(
				null, 
				dto.getTitle(), 
				dto.getDirector(), 
				dto.getStudio(), 
				dto.getMovieCast(),
				dto.getReleaseYear(), 
				posterUrl, 
				posterName);
	}

	/**
	 * Helper method to map a Movie entity to a MovieDto.
	 */ 
	private MovieDto mapToMovieDto(Movie movie) {
		return new MovieDto(
				movie.getMovieId(),
				movie.getTitle(), 
				movie.getDirector(),
				movie.getStudio(),
				movie.getMovieCast(), 
				movie.getReleaseYear(), 
				movie.getPosterUrl(),
				movie.getPosterName());
	}
	
}
