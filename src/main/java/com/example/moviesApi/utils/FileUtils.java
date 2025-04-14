package com.example.moviesApi.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Constants
    public static final String POSTER_UPLOAD_PATH =AppConstants.POSTER_UPLOAD_PATH ;
    public static final String BASE_URL =AppConstants.BASE_URL;

    /**
     * Builds the file path from base path and filename.
     *
     * @param path     the base directory
     * @param filename the name of the file
     * @return Path object pointing to the file
     */
    public static Path buildFilePath(String path, String filename) {
        return Paths.get(path, filename);
    }

    /**
     * Builds the file URL from base URL and filename.
     *
     * @param baseUrl  the base URL
     * @param filename the name of the file
     * @return the full URL to access the file
     */
    public static String buildFileUrl(String baseUrl, String filename) {
        return baseUrl.endsWith("/") ? baseUrl + filename : baseUrl + "/" + filename;
    }

    public static Path getPosterPath(String filename) {
        return buildFilePath(POSTER_UPLOAD_PATH, filename);
    }

    public static String getPosterUrl(String filename) {
        return buildFileUrl(BASE_URL, filename);
    }
}