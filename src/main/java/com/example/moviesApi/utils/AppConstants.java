package com.example.moviesApi.utils;

public class AppConstants {
	 private AppConstants() {
	        throw new UnsupportedOperationException("Utility class");
	    }

	    // File-related constants
	    public static final String BASE_URL = "http://localhost:8080/";
	    public static final String POSTER_UPLOAD_PATH = "uploads/posters";

	    // Auth-related constants
	    public static final int TOKEN_EXPIRY_MINUTES = 60;

	    // Feature toggles
	    public static final boolean ENABLE_COOL_FEATURE = true;

	    // Other common constants
	    public static final String DATE_FORMAT = "yyyy-MM-dd";
	    public static final int MAX_FILE_SIZE_MB = 50;

}
