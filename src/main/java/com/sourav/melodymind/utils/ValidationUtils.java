package com.sourav.melodymind.utils;

import com.sourav.melodymind.constants.ApplicationConstants;

import java.util.regex.Pattern;

/**
 * Validation utility methods for MelodyMind application.
 * Provides common validation functions for various data types.
 * 
 * @author MelodyMind Team
 * @version 1.0
 * @since 1.0
 */
public final class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(ApplicationConstants.REGEX_EMAIL);
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(ApplicationConstants.REGEX_ALPHANUMERIC);
    private static final Pattern SONG_TITLE_PATTERN = Pattern.compile(ApplicationConstants.REGEX_SONG_TITLE);
    private static final Pattern ARTIST_NAME_PATTERN = Pattern.compile(ApplicationConstants.REGEX_ARTIST_NAME);
    
    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Validates if a user ID is valid.
     * 
     * @param userId the user ID to validate
     * @return true if valid
     */
    public static boolean isValidUserId(Long userId) {
        return userId != null && userId > 0;
    }
    
    /**
     * Validates if a song ID is valid.
     * 
     * @param songId the song ID to validate
     * @return true if valid
     */
    public static boolean isValidSongId(Long songId) {
        return songId != null && songId > 0;
    }
    
    /**
     * Validates if a limit value is within acceptable range.
     * 
     * @param limit the limit to validate
     * @return true if valid (between 1 and 100)
     */
    public static boolean isValidLimit(Integer limit) {
        return limit != null && limit >= 1 && limit <= 100;
    }
    
    /**
     * Validates if an artist name is valid.
     * 
     * @param artist the artist name to validate
     * @return true if valid
     */
    public static boolean isValidArtistName(String artist) {
        if (StringUtils.isBlank(artist)) {
            return false;
        }
        
        String trimmed = artist.trim();
        return trimmed.length() >= 1 && 
               trimmed.length() <= 100 && 
               ARTIST_NAME_PATTERN.matcher(trimmed).matches();
    }
    
    /**
     * Validates if a song title is valid.
     * 
     * @param title the song title to validate
     * @return true if valid
     */
    public static boolean isValidSongTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return false;
        }
        
        String trimmed = title.trim();
        return trimmed.length() >= 1 && 
               trimmed.length() <= 200 && 
               SONG_TITLE_PATTERN.matcher(trimmed).matches();
    }
    
    /**
     * Validates if a genre is valid.
     * 
     * @param genre the genre to validate
     * @return true if valid
     */
    public static boolean isValidGenre(String genre) {
        if (StringUtils.isBlank(genre)) {
            return true; // Genre is optional
        }
        
        return ApplicationConstants.POPULAR_GENRES.contains(genre.toLowerCase());
    }
    
    /**
     * Validates if a mood is valid.
     * 
     * @param mood the mood to validate
     * @return true if valid
     */
    public static boolean isValidMood(String mood) {
        if (StringUtils.isBlank(mood)) {
            return true; // Mood is optional
        }
        
        return ApplicationConstants.POPULAR_MOODS.contains(mood.toLowerCase());
    }
    
    /**
     * Validates if a region code is valid.
     * 
     * @param region the region code to validate
     * @return true if valid
     */
    public static boolean isValidRegion(String region) {
        if (StringUtils.isBlank(region)) {
            return true; // Region is optional
        }
        
        return ApplicationConstants.POPULAR_REGIONS.contains(region.toUpperCase());
    }
    
    /**
     * Validates if a language code is valid.
     * 
     * @param language the language code to validate
     * @return true if valid
     */
    public static boolean isValidLanguage(String language) {
        if (StringUtils.isBlank(language)) {
            return true; // Language is optional
        }
        
        return ApplicationConstants.POPULAR_LANGUAGES.contains(language.toLowerCase());
    }
    
    /**
     * Validates if an email address is valid.
     * 
     * @param email the email to validate
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates if a string contains only alphanumeric characters.
     * 
     * @param str the string to validate
     * @return true if alphanumeric
     */
    public static boolean isAlphanumeric(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        
        return ALPHANUMERIC_PATTERN.matcher(str).matches();
    }
    
    /**
     * Validates if a timeout value is valid.
     * 
     * @param timeout the timeout in milliseconds
     * @return true if valid (between 1000 and 30000)
     */
    public static boolean isValidTimeout(Integer timeout) {
        return timeout != null && timeout >= 1000 && timeout <= 30000;
    }
    
    /**
     * Validates if a retry attempts value is valid.
     * 
     * @param retryAttempts the number of retry attempts
     * @return true if valid (between 0 and 10)
     */
    public static boolean isValidRetryAttempts(Integer retryAttempts) {
        return retryAttempts != null && retryAttempts >= 0 && retryAttempts <= 10;
    }
    
    /**
     * Validates if a batch size is valid.
     * 
     * @param batchSize the batch size
     * @return true if valid (between 1 and 1000)
     */
    public static boolean isValidBatchSize(Integer batchSize) {
        return batchSize != null && batchSize >= 1 && batchSize <= 1000;
    }
    
    /**
     * Validates if a data retention period is valid.
     * 
     * @param days the number of days
     * @return true if valid (between 1 and 365)
     */
    public static boolean isValidRetentionDays(Integer days) {
        return days != null && days >= 1 && days <= 365;
    }
    
    /**
     * Validates if a popularity score is valid.
     * 
     * @param score the popularity score
     * @return true if valid (between 0.0 and 100.0)
     */
    public static boolean isValidPopularityScore(Double score) {
        return score != null && score >= 0.0 && score <= 100.0;
    }
    
    /**
     * Validates if a URL is well-formed.
     * 
     * @param url the URL to validate
     * @return true if valid
     */
    public static boolean isValidUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        
        try {
            new java.net.URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }
    
    /**
     * Validates if a provider name is valid.
     * 
     * @param providerName the provider name
     * @return true if valid
     */
    public static boolean isValidProviderName(String providerName) {
        if (StringUtils.isBlank(providerName)) {
            return false;
        }
        
        return providerName.equals(ApplicationConstants.PROVIDER_SPOTIFY) ||
               providerName.equals(ApplicationConstants.PROVIDER_LASTFM) ||
               providerName.equals(ApplicationConstants.PROVIDER_DEEZER) ||
               providerName.equals(ApplicationConstants.PROVIDER_LYRICS_OVH) ||
               providerName.equals(ApplicationConstants.PROVIDER_GENIUS);
    }
    
    /**
     * Validates if a cache name is valid.
     * 
     * @param cacheName the cache name
     * @return true if valid
     */
    public static boolean isValidCacheName(String cacheName) {
        if (StringUtils.isBlank(cacheName)) {
            return false;
        }
        
        return cacheName.equals(ApplicationConstants.CACHE_RECOMMENDATIONS) ||
               cacheName.equals(ApplicationConstants.CACHE_TRENDING) ||
               cacheName.equals(ApplicationConstants.CACHE_LYRICS) ||
               cacheName.equals(ApplicationConstants.CACHE_CHORDS);
    }
}