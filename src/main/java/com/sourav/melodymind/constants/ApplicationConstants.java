package com.sourav.melodymind.constants;

import java.util.List;

/**
 * Application-wide constants for MelodyMind.
 * Contains string literals, configuration values, and business constants.
 * 
 * @author MelodyMind Team
 * @version 1.0
 * @since 1.0
 */
public final class ApplicationConstants {
    
    // Private constructor to prevent instantiation
    private ApplicationConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // Application metadata
    public static final String APPLICATION_NAME = "MelodyMind";
    public static final String APPLICATION_DESCRIPTION = "Smart Music Recommendation Dashboard";
    public static final String APPLICATION_VERSION = "1.0.0";
    public static final String APPLICATION_AUTHOR = "MelodyMind Team";
    
    // Provider names
    public static final String PROVIDER_SPOTIFY = "Spotify";
    public static final String PROVIDER_LASTFM = "LastFM";
    public static final String PROVIDER_DEEZER = "Deezer";
    public static final String PROVIDER_LYRICS_OVH = "LyricsOvh";
    public static final String PROVIDER_GENIUS = "Genius";
    
    // Cache names
    public static final String CACHE_RECOMMENDATIONS = "recommendations";
    public static final String CACHE_TRENDING = "trending";
    public static final String CACHE_LYRICS = "lyrics";
    public static final String CACHE_CHORDS = "chords";
    
    // Default values
    public static final int DEFAULT_LIMIT = 20;
    public static final int DEFAULT_TIMEOUT = 5000;
    public static final int DEFAULT_RETRY_ATTEMPTS = 3;
    public static final int DEFAULT_BATCH_SIZE = 100;
    public static final int DEFAULT_DATA_RETENTION_DAYS = 7;
    public static final String DEFAULT_REGION = "US";
    public static final String DEFAULT_LANGUAGE = "en";
    
    // Status values
    public static final String STATUS_UP = "UP";
    public static final String STATUS_DOWN = "DOWN";
    public static final String STATUS_HEALTHY = "HEALTHY";
    public static final String STATUS_OVERDUE = "OVERDUE";
    public static final String STATUS_NEVER_RUN = "NEVER_RUN";
    public static final String STATUS_UNKNOWN = "UNKNOWN";
    
    // Response messages
    public static final String MSG_SUCCESS = "Operation completed successfully";
    public static final String MSG_ERROR = "An error occurred";
    public static final String MSG_NOT_FOUND = "Resource not found";
    public static final String MSG_INVALID_REQUEST = "Invalid request";
    public static final String MSG_UNAUTHORIZED = "Unauthorized access";
    public static final String MSG_FORBIDDEN = "Access forbidden";
    public static final String MSG_INTERNAL_ERROR = "Internal server error";
    
    // Music-specific messages
    public static final String MSG_RECOMMENDATIONS_FETCHED = "Recommendations fetched successfully";
    public static final String MSG_TRENDING_FETCHED = "Trending songs fetched successfully";
    public static final String MSG_LYRICS_FETCHED = "Lyrics fetched successfully";
    public static final String MSG_CHORDS_FETCHED = "Piano chords fetched successfully";
    public static final String MSG_FAVORITES_FETCHED = "User favorites fetched successfully";
    public static final String MSG_FAVORITE_ADDED = "Song added to favorites successfully";
    public static final String MSG_FAVORITE_REMOVED = "Song removed from favorites successfully";
    public static final String MSG_FAVORITE_ALREADY_EXISTS = "Song is already in favorites";
    public static final String MSG_FAVORITE_NOT_EXISTS = "Song was not in favorites";
    public static final String MSG_FAVORITE_COUNT_FETCHED = "Favorite count fetched successfully";
    
    // Provider messages
    public static final String MSG_PROVIDER_STATUS_FETCHED = "Provider status fetched successfully";
    public static final String MSG_PROVIDER_LIST_FETCHED = "Provider list fetched successfully";
    public static final String MSG_ENABLED_PROVIDERS_FETCHED = "Enabled providers fetched successfully";
    
    // Scheduler messages
    public static final String MSG_SCHEDULER_STATS_FETCHED = "Scheduler statistics fetched successfully";
    public static final String MSG_MANUAL_CLEANUP_COMPLETED = "Manual cleanup completed successfully";
    public static final String MSG_TRENDING_REFRESH_COMPLETED = "Trending songs refresh completed successfully";
    public static final String MSG_CACHE_CLEARED = "Cache cleared successfully";
    public static final String MSG_PRELOAD_COMPLETED = "Recommendations preloaded successfully";
    
    // Test messages
    public static final String MSG_LYRICS_TEST_COMPLETED = "Lyrics test completed";
    public static final String MSG_CHORDS_TEST_COMPLETED = "Chords test completed";
    public static final String MSG_ASYNC_TEST_COMPLETED = "Async test completed";
    public static final String MSG_THREAD_INFO_RETRIEVED = "Thread information retrieved";
    
    // Health messages
    public static final String MSG_SERVICE_HEALTHY = "Service is healthy";
    
    // Error messages
    public static final String ERR_RECOMMENDATIONS_FAILED = "Failed to fetch recommendations";
    public static final String ERR_TRENDING_FAILED = "Failed to fetch trending songs";
    public static final String ERR_LYRICS_FAILED = "Failed to fetch lyrics";
    public static final String ERR_CHORDS_FAILED = "Failed to fetch piano chords";
    public static final String ERR_FAVORITES_FAILED = "Failed to load favorites";
    public static final String ERR_PROVIDER_STATUS_FAILED = "Failed to load provider status";
    public static final String ERR_SCHEDULER_STATS_FAILED = "Failed to load scheduler stats";
    public static final String ERR_MANUAL_CLEANUP_FAILED = "Manual cleanup failed";
    public static final String ERR_TRENDING_REFRESH_FAILED = "Trending refresh failed";
    public static final String ERR_CACHE_CLEAR_FAILED = "Cache clear failed";
    public static final String ERR_PRELOAD_FAILED = "Preload failed";
    public static final String ERR_SONG_NOT_FOUND = "Song not found";
    public static final String ERR_USER_NOT_FOUND = "User not found";
    public static final String ERR_VALIDATION_FAILED = "Validation failed";
    public static final String ERR_MISSING_PARAMETER = "Missing required parameter";
    public static final String ERR_INVALID_PARAMETER = "Invalid parameter value";
    public static final String ERR_API_UNAVAILABLE = "External API unavailable";
    public static final String ERR_TIMEOUT = "Request timeout";
    public static final String ERR_RATE_LIMIT = "Rate limit exceeded";
    
    // Validation messages
    public static final String VALIDATION_USER_ID_REQUIRED = "User ID is required";
    public static final String VALIDATION_SONG_ID_REQUIRED = "Song ID is required";
    public static final String VALIDATION_ARTIST_REQUIRED = "Artist is required";
    public static final String VALIDATION_TITLE_REQUIRED = "Title is required";
    public static final String VALIDATION_INVALID_LIMIT = "Limit must be between 1 and 100";
    public static final String VALIDATION_INVALID_USER_ID = "Invalid user ID format";
    public static final String VALIDATION_INVALID_SONG_ID = "Invalid song ID format";
    
    // Music genres
    public static final List<String> POPULAR_GENRES = List.of(
        "pop", "rock", "hip-hop", "electronic", "jazz", "classical", 
        "country", "r&b", "reggae", "blues", "folk", "metal"
    );
    
    // Music moods
    public static final List<String> POPULAR_MOODS = List.of(
        "happy", "sad", "energetic", "chill", "focus", "romantic", 
        "dramatic", "nostalgic", "uplifting", "melancholic", "peaceful", "intense"
    );
    
    // Regions
    public static final List<String> POPULAR_REGIONS = List.of(
        "US", "UK", "CA", "AU", "DE", "FR", "JP", "BR", 
        "IT", "ES", "NL", "SE", "NO", "DK", "FI"
    );
    
    // Languages
    public static final List<String> POPULAR_LANGUAGES = List.of(
        "en", "es", "fr", "de", "it", "pt", "ja", "ko", 
        "zh", "ru", "ar", "hi", "nl", "sv", "no", "da", "fi"
    );
    
    // Chord progressions
    public static final List<String> POP_CHORD_PROGRESSIONS = List.of(
        "C - G - Am - F", "G - D - Em - C", "F - C - G - Am", "Am - F - C - G"
    );
    
    public static final List<String> ROCK_CHORD_PROGRESSIONS = List.of(
        "E - A - B - E", "A - D - E - A", "G - C - D - G", "Em - C - G - D"
    );
    
    public static final List<String> JAZZ_CHORD_PROGRESSIONS = List.of(
        "Cmaj7 - Am7 - Dm7 - G7", "Fmaj7 - Em7 - Am7 - Dm7", "Gmaj7 - Em7 - Am7 - D7"
    );
    
    // Time constants
    public static final long ONE_HOUR_MILLIS = 3600000L;
    public static final long ONE_DAY_MILLIS = 86400000L;
    public static final long ONE_WEEK_MILLIS = 604800000L;
    public static final int ONE_MINUTE_SECONDS = 60;
    public static final int ONE_HOUR_SECONDS = 3600;
    public static final int ONE_DAY_SECONDS = 86400;
    
    // File and resource constants
    public static final String STATIC_RESOURCES_PATH = "classpath:/static/";
    public static final String TEMPLATES_PATH = "classpath:/templates/";
    public static final String LOG_FILE_NAME = "logs/melodymind.log";
    public static final String LOG_FILE_MAX_SIZE = "10MB";
    public static final int LOG_FILE_MAX_HISTORY = 30;
    
    // Thread pool constants
    public static final int CORE_POOL_SIZE = 10;
    public static final int MAX_POOL_SIZE = 50;
    public static final int QUEUE_CAPACITY = 100;
    public static final String THREAD_NAME_PREFIX = "melodymind-";
    
    // Database constants
    public static final String H2_CONSOLE_PATH = "/h2-console";
    public static final String H2_DRIVER_CLASS = "org.h2.Driver";
    public static final String H2_URL_PREFIX = "jdbc:h2:mem:";
    public static final String H2_USERNAME = "sa";
    public static final String H2_PASSWORD = "";
    
    // HTTP constants
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_BEARER_PREFIX = "Bearer ";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String USER_AGENT_VALUE = APPLICATION_NAME + "/" + APPLICATION_VERSION;
    
    // Regex patterns
    public static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    public static final String REGEX_ALPHANUMERIC = "^[a-zA-Z0-9]+$";
    public static final String REGEX_SONG_TITLE = "^[a-zA-Z0-9\\s\\-_'\"().,!?]+$";
    public static final String REGEX_ARTIST_NAME = "^[a-zA-Z0-9\\s\\-_'\"().,&]+$";
    
    // Configuration keys
    public static final String CONFIG_CACHE_REFRESH_INTERVAL = "melodymind.cache.refresh-interval";
    public static final String CONFIG_API_TIMEOUT = "melodymind.api.timeout";
    public static final String CONFIG_RETRY_ATTEMPTS = "melodymind.api.retry-attempts";
    public static final String CONFIG_SCHEDULER_ENABLED = "melodymind.scheduler.enabled";
    public static final String CONFIG_DATA_RETENTION_DAYS = "melodymind.scheduler.old-data-retention-days";
}