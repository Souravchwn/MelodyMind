package com.sourav.melodymind.utils;

import com.sourav.melodymind.constants.ApplicationConstants;

/**
 * Cache utility methods for MelodyMind application.
 * Provides common cache key generation and management functions.
 * 
 * @author MelodyMind Team
 * @version 1.0
 * @since 1.0
 */
public final class CacheUtils {
    
    private static final String CACHE_KEY_SEPARATOR = "_";
    private static final String ANONYMOUS_USER = "anonymous";
    
    // Private constructor to prevent instantiation
    private CacheUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Generates a cache key for recommendations.
     * 
     * @param genre the genre filter
     * @param language the language filter
     * @param region the region filter
     * @param mood the mood filter
     * @param limit the result limit
     * @return generated cache key
     */
    public static String generateRecommendationsCacheKey(String genre, String language, 
                                                        String region, String mood, Integer limit) {
        StringBuilder keyBuilder = new StringBuilder();
        
        keyBuilder.append(StringUtils.defaultIfNull(genre, "all"));
        keyBuilder.append(CACHE_KEY_SEPARATOR);
        keyBuilder.append(StringUtils.defaultIfNull(language, "all"));
        keyBuilder.append(CACHE_KEY_SEPARATOR);
        keyBuilder.append(StringUtils.defaultIfNull(region, "all"));
        keyBuilder.append(CACHE_KEY_SEPARATOR);
        keyBuilder.append(StringUtils.defaultIfNull(mood, "all"));
        keyBuilder.append(CACHE_KEY_SEPARATOR);
        keyBuilder.append(limit != null ? limit : ApplicationConstants.DEFAULT_LIMIT);
        
        return StringUtils.normalizeForKey(keyBuilder.toString());
    }
    
    /**
     * Generates a cache key for trending songs.
     * 
     * @param region the region filter
     * @param userId the user ID (optional)
     * @return generated cache key
     */
    public static String generateTrendingCacheKey(String region, Long userId) {
        StringBuilder keyBuilder = new StringBuilder();
        
        keyBuilder.append(StringUtils.defaultIfNull(region, ApplicationConstants.DEFAULT_REGION));
        keyBuilder.append(CACHE_KEY_SEPARATOR);
        keyBuilder.append(userId != null ? userId.toString() : ANONYMOUS_USER);
        
        return StringUtils.normalizeForKey(keyBuilder.toString());
    }
    
    /**
     * Generates a cache key for lyrics.
     * 
     * @param artist the artist name
     * @param title the song title
     * @return generated cache key
     */
    public static String generateLyricsCacheKey(String artist, String title) {
        return StringUtils.generateCacheKey(artist, title);
    }
    
    /**
     * Generates a cache key for piano chords.
     * 
     * @param artist the artist name
     * @param title the song title
     * @return generated cache key
     */
    public static String generateChordsCacheKey(String artist, String title) {
        return StringUtils.generateCacheKey(artist, title);
    }
    
    /**
     * Generates a cache key for user favorites.
     * 
     * @param userId the user ID
     * @return generated cache key
     */
    public static String generateFavoritesCacheKey(Long userId) {
        return "user_" + (userId != null ? userId.toString() : ANONYMOUS_USER);
    }
    
    /**
     * Generates a cache key for provider status.
     * 
     * @param providerName the provider name
     * @return generated cache key
     */
    public static String generateProviderStatusCacheKey(String providerName) {
        return "provider_" + StringUtils.normalizeForKey(providerName);
    }
    
    /**
     * Generates a cache key for scheduler statistics.
     * 
     * @return generated cache key
     */
    public static String generateSchedulerStatsCacheKey() {
        return "scheduler_stats";
    }
    
    /**
     * Validates if a cache key is well-formed.
     * 
     * @param cacheKey the cache key to validate
     * @return true if the cache key is valid
     */
    public static boolean isValidCacheKey(String cacheKey) {
        if (StringUtils.isBlank(cacheKey)) {
            return false;
        }
        
        // Cache keys should not be too long
        if (cacheKey.length() > 250) {
            return false;
        }
        
        // Cache keys should only contain safe characters
        return cacheKey.matches("^[a-zA-Z0-9_-]+$");
    }
    
    /**
     * Sanitizes a cache key to ensure it's safe to use.
     * 
     * @param cacheKey the cache key to sanitize
     * @return sanitized cache key
     */
    public static String sanitizeCacheKey(String cacheKey) {
        if (StringUtils.isBlank(cacheKey)) {
            return "empty_key";
        }
        
        // Normalize and limit length
        String sanitized = StringUtils.normalizeForKey(cacheKey);
        
        if (sanitized.length() > 250) {
            sanitized = sanitized.substring(0, 250);
        }
        
        // Ensure it's not empty after sanitization
        return StringUtils.isBlank(sanitized) ? "sanitized_key" : sanitized;
    }
    
    /**
     * Extracts cache statistics information from a cache key.
     * 
     * @param cacheKey the cache key
     * @return cache key information
     */
    public static CacheKeyInfo extractCacheKeyInfo(String cacheKey) {
        if (StringUtils.isBlank(cacheKey)) {
            return new CacheKeyInfo("unknown", "unknown", false);
        }
        
        String[] parts = cacheKey.split(CACHE_KEY_SEPARATOR);
        String type = parts.length > 0 ? parts[0] : "unknown";
        String category = determineCacheCategory(cacheKey);
        boolean isUserSpecific = cacheKey.contains("user_") || cacheKey.contains(ANONYMOUS_USER);
        
        return new CacheKeyInfo(type, category, isUserSpecific);
    }
    
    /**
     * Determines the cache category based on the cache key pattern.
     * 
     * @param cacheKey the cache key
     * @return cache category
     */
    private static String determineCacheCategory(String cacheKey) {
        if (cacheKey.contains("recommendation")) {
            return ApplicationConstants.CACHE_RECOMMENDATIONS;
        } else if (cacheKey.contains("trending")) {
            return ApplicationConstants.CACHE_TRENDING;
        } else if (cacheKey.contains("lyrics")) {
            return ApplicationConstants.CACHE_LYRICS;
        } else if (cacheKey.contains("chord")) {
            return ApplicationConstants.CACHE_CHORDS;
        } else if (cacheKey.contains("favorite")) {
            return "favorites";
        } else if (cacheKey.contains("provider")) {
            return "providers";
        } else if (cacheKey.contains("scheduler")) {
            return "scheduler";
        } else {
            return "other";
        }
    }
    
    /**
     * Cache key information holder.
     */
    public static class CacheKeyInfo {
        private final String type;
        private final String category;
        private final boolean userSpecific;
        
        public CacheKeyInfo(String type, String category, boolean userSpecific) {
            this.type = type;
            this.category = category;
            this.userSpecific = userSpecific;
        }
        
        public String getType() {
            return type;
        }
        
        public String getCategory() {
            return category;
        }
        
        public boolean isUserSpecific() {
            return userSpecific;
        }
        
        @Override
        public String toString() {
            return String.format("CacheKeyInfo{type='%s', category='%s', userSpecific=%s}", 
                               type, category, userSpecific);
        }
    }
}