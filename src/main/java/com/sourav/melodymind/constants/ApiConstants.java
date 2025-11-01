package com.sourav.melodymind.constants;

/**
 * API endpoint constants for MelodyMind application.
 * Contains all REST API endpoint paths and patterns.
 * 
 * @author MelodyMind Team
 * @version 1.0
 * @since 1.0
 */
public final class ApiConstants {
    
    // Private constructor to prevent instantiation
    private ApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // Base API paths
    public static final String API_BASE = "/api";
    public static final String API_V1 = "/api/v1";
    
    // Health endpoints
    public static final String HEALTH = "/health";
    public static final String HEALTH_FULL_PATH = API_BASE + HEALTH;
    
    // Music endpoints
    public static final String MUSIC_BASE = "/music";
    public static final String MUSIC_FULL_PATH = API_BASE + MUSIC_BASE;
    public static final String MUSIC_RECOMMENDATIONS = "/recommendations";
    public static final String MUSIC_TRENDING = "/trending";
    public static final String MUSIC_LYRICS = "/lyrics";
    public static final String MUSIC_CHORDS = "/chords";
    public static final String MUSIC_RECOMMENDATIONS_FULL_PATH = MUSIC_FULL_PATH + MUSIC_RECOMMENDATIONS;
    public static final String MUSIC_TRENDING_FULL_PATH = MUSIC_FULL_PATH + MUSIC_TRENDING;
    public static final String MUSIC_LYRICS_FULL_PATH = MUSIC_FULL_PATH + MUSIC_LYRICS;
    public static final String MUSIC_CHORDS_FULL_PATH = MUSIC_FULL_PATH + MUSIC_CHORDS;
    
    // Favorites endpoints
    public static final String FAVORITES_BASE = "/favorites";
    public static final String FAVORITES_FULL_PATH = MUSIC_FULL_PATH + FAVORITES_BASE;
    public static final String FAVORITES_ADD = "/add";
    public static final String FAVORITES_REMOVE = "/remove";
    public static final String FAVORITES_CHECK = "/check";
    public static final String FAVORITES_COUNT = "/count";
    public static final String FAVORITES_ADD_FULL_PATH = FAVORITES_FULL_PATH + FAVORITES_ADD;
    public static final String FAVORITES_REMOVE_FULL_PATH = FAVORITES_FULL_PATH + FAVORITES_REMOVE;
    public static final String FAVORITES_CHECK_FULL_PATH = FAVORITES_FULL_PATH + FAVORITES_CHECK;
    public static final String FAVORITES_COUNT_FULL_PATH = FAVORITES_FULL_PATH + FAVORITES_COUNT;
    
    // Provider endpoints
    public static final String PROVIDERS_BASE = "/providers";
    public static final String PROVIDERS_FULL_PATH = API_BASE + PROVIDERS_BASE;
    public static final String PROVIDERS_STATUS = "/status";
    public static final String PROVIDERS_LIST = "/list";
    public static final String PROVIDERS_ENABLED = "/enabled";
    public static final String PROVIDERS_STATUS_FULL_PATH = PROVIDERS_FULL_PATH + PROVIDERS_STATUS;
    public static final String PROVIDERS_LIST_FULL_PATH = PROVIDERS_FULL_PATH + PROVIDERS_LIST;
    public static final String PROVIDERS_ENABLED_FULL_PATH = PROVIDERS_FULL_PATH + PROVIDERS_ENABLED;
    
    // Scheduler endpoints
    public static final String SCHEDULER_BASE = "/scheduler";
    public static final String SCHEDULER_FULL_PATH = API_BASE + SCHEDULER_BASE;
    public static final String SCHEDULER_STATS = "/stats";
    public static final String SCHEDULER_CLEANUP_MANUAL = "/cleanup/manual";
    public static final String SCHEDULER_TRENDING_REFRESH = "/trending/refresh";
    public static final String SCHEDULER_CACHE_CLEAR = "/cache/clear";
    public static final String SCHEDULER_PRELOAD = "/preload";
    public static final String SCHEDULER_STATS_FULL_PATH = SCHEDULER_FULL_PATH + SCHEDULER_STATS;
    public static final String SCHEDULER_CLEANUP_MANUAL_FULL_PATH = SCHEDULER_FULL_PATH + SCHEDULER_CLEANUP_MANUAL;
    public static final String SCHEDULER_TRENDING_REFRESH_FULL_PATH = SCHEDULER_FULL_PATH + SCHEDULER_TRENDING_REFRESH;
    public static final String SCHEDULER_CACHE_CLEAR_FULL_PATH = SCHEDULER_FULL_PATH + SCHEDULER_CACHE_CLEAR;
    public static final String SCHEDULER_PRELOAD_FULL_PATH = SCHEDULER_FULL_PATH + SCHEDULER_PRELOAD;
    
    // Test endpoints
    public static final String TEST_BASE = "/test";
    public static final String TEST_FULL_PATH = API_BASE + TEST_BASE;
    public static final String TEST_LYRICS = "/lyrics";
    public static final String TEST_CHORDS = "/chords";
    public static final String TEST_ASYNC = "/async-test";
    public static final String TEST_THREAD_INFO = "/thread-info";
    public static final String TEST_LYRICS_FULL_PATH = TEST_FULL_PATH + TEST_LYRICS;
    public static final String TEST_CHORDS_FULL_PATH = TEST_FULL_PATH + TEST_CHORDS;
    public static final String TEST_ASYNC_FULL_PATH = TEST_FULL_PATH + TEST_ASYNC;
    public static final String TEST_THREAD_INFO_FULL_PATH = TEST_FULL_PATH + TEST_THREAD_INFO;
    
    // Web endpoints
    public static final String WEB_ROOT = "/";
    public static final String WEB_DASHBOARD = "/dashboard";
    public static final String WEB_ADMIN = "/admin";
    public static final String WEB_TEST = "/test";
    public static final String WEB_ABOUT = "/about";
    
    // External API endpoints
    public static final String LYRICS_OVH_BASE = "https://api.lyrics.ovh";
    public static final String LYRICS_OVH_V1 = LYRICS_OVH_BASE + "/v1";
    public static final String GENIUS_BASE = "https://api.genius.com";
    public static final String SPOTIFY_BASE = "https://api.spotify.com/v1";
    public static final String LASTFM_BASE = "https://ws.audioscrobbler.com/2.0";
    public static final String DEEZER_BASE = "https://api.deezer.com";
    
    // Path parameters
    public static final String PARAM_ARTIST = "artist";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_SONG_ID = "songId";
    public static final String PARAM_GENRE = "genre";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_REGION = "region";
    public static final String PARAM_MOOD = "mood";
    public static final String PARAM_LIMIT = "limit";
    
    // Path variables
    public static final String PATH_VAR_ARTIST = "/{" + PARAM_ARTIST + "}";
    public static final String PATH_VAR_TITLE = "/{" + PARAM_TITLE + "}";
    public static final String PATH_VAR_USER_ID = "/{" + PARAM_USER_ID + "}";
    public static final String PATH_VAR_SONG_ID = "/{" + PARAM_SONG_ID + "}";
}