package com.example.moviesApi.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.StreamUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.moviesApi.services.PosterService;


import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file/")
public class PosterController {

	private final PosterService posterService;
	private final String postersFolderPath;

	public PosterController(PosterService posterService) {
		this.posterService = posterService;
		this.postersFolderPath = "uploads/posters";
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadPosterHandler(@RequestPart("posterImageFile") MultipartFile posterImageFile) {
	    try {
	        if (posterImageFile.isEmpty()) {
	            return ResponseEntity.badRequest().body("Please select a file");
	        }

	        String fileName = posterService.uploadPoster(postersFolderPath, posterImageFile);
	        return ResponseEntity.ok("File Uploaded: " + fileName);
	    } catch (IOException e) {
	        e.printStackTrace(); // or use a logger
	        return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
	    }
	}
	
	@GetMapping("/{fileName}")
	public void downloadPosterHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
	    try {
	    	InputStream resourceFile = posterService.downloadPoster(postersFolderPath, fileName);
	        // Verify the file exists and is readable

	        // Set response headers
	        response.setContentType(MediaType.IMAGE_PNG_VALUE);
	        response.setHeader("Cache-Control", "max-age=3600"); // Optional caching
	        
	        // Stream the file content
	        StreamUtils.copy(resourceFile, response.getOutputStream());
	        response.flushBuffer();
	        
	    } catch (IOException e) {
	        // Log the error properly in production (using SLF4J/Log4j)
	    	 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	         response.getWriter().write("File not found: " + fileName);
	    }
	}
}














