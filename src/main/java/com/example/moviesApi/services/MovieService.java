package com.example.moviesApi.services;

import java.io.IOException;

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

	public MovieService(MovieRepository movieRepo) {
		this.movieRepository = movieRepo;
		this.posterService = new PosterService();
	}
	
	/*----------------GET Movie By ID----------------*/
	public MovieDto getMovieById(Integer id) {
	    Movie movie = movieRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
	    return mapToMovieDto(movie);
	}

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

	
	
	
	
	
	
	
	//===============HELPERS FUNCTION================
	
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
