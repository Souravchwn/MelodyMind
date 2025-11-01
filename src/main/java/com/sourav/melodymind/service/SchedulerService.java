package com.sourav.melodymind.service;

public interface SchedulerService {
    
    /**
     * Daily cleanup task - runs at 12 AM
     * Clears previous day data and refreshes trending songs
     */
    void performDailyCleanup();
    
    /**
     * Refresh trending songs for all regions
     */
    void refreshTrendingSongs();
    
    /**
     * Clear expired cache entries
     */
    void clearExpiredCache();
    
    /**
     * Clean old song data from database
     */
    void cleanOldSongData();
    
    /**
     * Preload popular recommendations
     */
    void preloadPopularRecommendations();
    
    /**
     * Get scheduler statistics
     */
    SchedulerStats getSchedulerStats();
}