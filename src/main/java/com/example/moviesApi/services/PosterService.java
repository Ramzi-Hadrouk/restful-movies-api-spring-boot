package com.example.moviesApi.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.moviesApi.utils.AppConstants;
import com.example.moviesApi.utils.FileUtils;

@Service
public class PosterService {

    // Constant to define the image MIME type prefix
    private static final String IMAGE_PREFIX = "image/";
    private static final String POSTER_UPLOAD_PATH =AppConstants.POSTER_UPLOAD_PATH;
    /**
     * Uploads a movie poster to the specified path.
     *
     * @param path the directory where the poster will be stored
     * @param file the poster file to upload
     * @return the sanitized file name of the uploaded poster
     * @throws IOException if any I/O error occurs or if the file is not a valid image
     */
    public String uploadPoster( MultipartFile file) throws IOException {
        String cleanedFileName = sanitizeFileName(file.getOriginalFilename());
        validateImageFile(file);
        Path fullPath = FileUtils.getPosterPath(cleanedFileName);
        createDirectoryIfNotExists(Paths.get( POSTER_UPLOAD_PATH));
        Files.copy(file.getInputStream(), fullPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        return cleanedFileName;
    }

    /**
     * Downloads a movie poster from the specified directory.
     * The method returns a PosterDownloadResponse depending on the provided option.
     *
     * @param path the directory where the poster is stored
     * @param filename the name of the poster file to download
     * @param option indicates whether to return the file's path or its InputStream
     * @return a PosterDownloadResponse object containing the file path or input stream
     * @throws IOException if the file is not found or cannot be read
     */
    public InputStream downloadPoster(String filename) throws IOException {
        // Build the full file path
        Path filePath = FileUtils.getPosterPath(filename);

        // Check if file exists
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath.toString());
        }
            // Get an InputStream from the file
        try {           
        	InputStream inputStream = Files.newInputStream(filePath);
        	return inputStream;
        	
        }catch(Exception e){       
            throw new IOException("Unsupported download option.");
            }


        
    }

    /**
     * Sanitizes the file name by cleaning it and ensuring it is valid.
     * If the cleaned name contains invalid sequences (like "..") or is empty,
     * a new file name is generated using a prefix, current time, and original extension.
     *
     * @param originalFileName the original file name from the uploaded file
     * @return a valid, sanitized file name
     */
    private String sanitizeFileName(String originalFileName) {
        String cleanedFileName = StringUtils.cleanPath(originalFileName);

        // Check if the cleaned file name is invalid
        if (cleanedFileName.contains("..") || cleanedFileName.trim().isEmpty()) {
            // Try to extract the extension from the original file name
            String extension = "";
            int dotIndex = cleanedFileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < cleanedFileName.length() - 1) {
                extension = cleanedFileName.substring(dotIndex);
            }
            // Generate a new file name with a prefix, current timestamp, and extension if available
            cleanedFileName = "poster_" + System.currentTimeMillis() + extension;
        }

        return cleanedFileName;
    }

    /**
     * Validates that the uploaded file is an image by checking its MIME type.
     *
     * @param file the file to validate
     * @throws IOException if the file is not a valid image
     */
    private void validateImageFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith(IMAGE_PREFIX)) {
            throw new IOException("The uploaded file is not a valid image for a movie poster.");
        }
    }

    /**
     * Creates the directory at the given path if it does not exist.
     *
     * @param directoryPath the path of the directory to create
     * @throws IOException if the directory cannot be created
     */
    private void createDirectoryIfNotExists(Path directoryPath) throws IOException {
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }
}




