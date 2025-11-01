package com.sourav.melodymind.service.impl;

import com.sourav.melodymind.dto.RecommendationRequestDto;
import com.sourav.melodymind.provider.MusicProviderPlugin;
import com.sourav.melodymind.provider.ProviderManager;
import com.sourav.melodymind.repository.SongRepository;
import com.sourav.melodymind.service.MusicRecommendationService;
import com.sourav.melodymind.service.SchedulerService;
import com.sourav.melodymind.service.SchedulerStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {
    
    private final MusicRecommendationService musicRecommendationService;
    private final SongRepository songRepository;
    private final CacheManager cacheManager;
    private final ProviderManager providerManager;
    private final com.sourav.melodymind.service.impl.UserSessionServiceImpl userSessionService;
    
    // Statistics tracking
    private final AtomicLong lastCleanupTime = new AtomicLong(0);
    private final AtomicLong lastTrendingRefresh = new AtomicLong(0);
    private final AtomicInteger cleanupCount = new AtomicInteger(0);
    private final AtomicInteger trendingRefreshCount = new AtomicInteger(0);
    private final AtomicLong songsCleanedUp = new AtomicLong(0);
    
    // Configuration constants
    private static final List<String> POPULAR_GENRES = Arrays.asList("pop", "rock", "hip-hop", "electronic", "jazz", "classical");
    private static final List<String> POPULAR_MOODS = Arrays.asList("happy", "sad", "energetic", "chill", "focus", "romantic");
    private static final List<String> POPULAR_REGIONS = Arrays.asList("US", "UK", "CA", "AU", "DE", "FR", "JP", "BR");
    private static final List<String> POPULAR_LANGUAGES = Arrays.asList("en", "es", "fr", "de", "ja", "pt");
    
    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Every day at 12:00 AM
    public void performDailyCleanup() {
        log.info("Starting daily cleanup task at 12:00 AM");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Step 1: Clear all caches
            clearExpiredCache();
            
            // Step 2: Clean old song data
            cleanOldSongData();
            
            // Step 3: Refresh trending songs
            refreshTrendingSongs();
            
            // Step 4: Preload popular recommendations
            preloadPopularRecommendations();
            
            // Step 5: Cleanup expired user sessions
            userSessionService.cleanupExpiredSessions();
            
            lastCleanupTime.set(System.currentTimeMillis());
            cleanupCount.incrementAndGet();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Daily cleanup completed successfully in {} ms", duration);
            
        } catch (Exception e) {
            log.error("Error during daily cleanup", e);
        }
    }
    
    @Override
    public void refreshTrendingSongs() {
        log.info("Refreshing trending songs for all regions");
        
        try {
            List<CompletableFuture<Void>> futures = POPULAR_REGIONS.stream()
                    .map(region -> CompletableFuture.runAsync(() -> {
                        try {
                            log.debug("Refreshing trending songs for region: {}", region);
                            musicRecommendationService.getTrendingSongs(region, null);
                        } catch (Exception e) {
                            log.error("Error refreshing trending songs for region {}: {}", region, e.getMessage());
                        }
                    }))
                    .toList();
            
            // Wait for all regions to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
            lastTrendingRefresh.set(System.currentTimeMillis());
            trendingRefreshCount.incrementAndGet();
            
            log.info("Trending songs refresh completed for {} regions", POPULAR_REGIONS.size());
            
        } catch (Exception e) {
            log.error("Error during trending songs refresh", e);
        }
    }
    
    @Override
    public void clearExpiredCache() {
        log.info("Clearing expired cache entries");
        
        try {
            cacheManager.getCacheNames().forEach(cacheName -> {
                var cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    // For recommendations cache, we want to clear it completely for daily refresh
                    if ("recommendations".equals(cacheName)) {
                        cache.clear();
                        log.debug("Cleared recommendations cache completely");
                    }
                    // For trending cache, clear it to get fresh data
                    else if ("trending".equals(cacheName)) {
                        cache.clear();
                        log.debug("Cleared trending cache completely");
                    }
                    // Keep lyrics and chords cache as they don't change frequently
                    log.debug("Processed cache: {}", cacheName);
                }
            });
            
            log.info("Cache cleanup completed");
            
        } catch (Exception e) {
            log.error("Error during cache cleanup", e);
        }
    }
    
    @Override
    @Transactional
    public void cleanOldSongData() {
        log.info("Cleaning old song data from database");
        
        try {
            // Remove songs older than 7 days that are not in favorites
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
            
            // First, get count of songs to be deleted for statistics
            List<Long> songsToDelete = songRepository.findOldSongsNotInFavorites(cutoffDate);
            long countToDelete = songsToDelete.size();
            
            if (countToDelete > 0) {
                // Delete old songs in batches to avoid memory issues
                int batchSize = 100;
                for (int i = 0; i < songsToDelete.size(); i += batchSize) {
                    int endIndex = Math.min(i + batchSize, songsToDelete.size());
                    List<Long> batch = songsToDelete.subList(i, endIndex);
                    songRepository.deleteByIdIn(batch);
                    log.debug("Deleted batch of {} old songs", batch.size());
                }
                
                songsCleanedUp.addAndGet(countToDelete);
                log.info("Cleaned up {} old songs from database", countToDelete);
            } else {
                log.info("No old songs found to clean up");
            }
            
        } catch (Exception e) {
            log.error("Error during old song data cleanup", e);
        }
    }
    
    @Override
    public void preloadPopularRecommendations() {
        log.info("Preloading popular recommendations");
        
        try {
            List<CompletableFuture<Void>> futures = POPULAR_GENRES.stream()
                    .flatMap(genre -> POPULAR_MOODS.stream()
                            .map(mood -> CompletableFuture.runAsync(() -> {
                                try {
                                    RecommendationRequestDto request = RecommendationRequestDto.builder()
                                            .genre(genre)
                                            .mood(mood)
                                            .limit(10) // Smaller limit for preloading
                                            .build();
                                    
                                    musicRecommendationService.getRecommendations(request);
                                    log.debug("Preloaded recommendations for genre: {}, mood: {}", genre, mood);
                                    
                                } catch (Exception e) {
                                    log.error("Error preloading recommendations for genre {} and mood {}: {}", 
                                            genre, mood, e.getMessage());
                                }
                            })))
                    .toList();
            
            // Wait for all preloading to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
            log.info("Popular recommendations preloading completed for {} combinations", futures.size());
            
        } catch (Exception e) {
            log.error("Error during popular recommendations preloading", e);
        }
    }
    
    @Override
    public SchedulerStats getSchedulerStats() {
        return SchedulerStats.builder()
                .lastCleanupTime(lastCleanupTime.get() > 0 ? 
                        LocalDateTime.ofEpochSecond(lastCleanupTime.get() / 1000, 0, java.time.ZoneOffset.UTC) : null)
                .lastTrendingRefresh(lastTrendingRefresh.get() > 0 ? 
                        LocalDateTime.ofEpochSecond(lastTrendingRefresh.get() / 1000, 0, java.time.ZoneOffset.UTC) : null)
                .totalCleanups(cleanupCount.get())
                .totalTrendingRefreshes(trendingRefreshCount.get())
                .totalSongsCleanedUp(songsCleanedUp.get())
                .enabledProviders(providerManager.getEnabledProviders().size())
                .cacheNames(cacheManager.getCacheNames())
                .build();
    }
    
    // Additional scheduled tasks
    
    @Scheduled(fixedRate = 3600000) // Every hour
    public void hourlyRecommendationsCacheCleanup() {
        log.debug("Performing hourly recommendations cache cleanup");
        
        var recommendationsCache = cacheManager.getCache("recommendations");
        if (recommendationsCache != null) {
            // The cache is configured to expire after 1 hour, so this is just for logging
            log.debug("Recommendations cache cleanup - cache will auto-expire entries older than 1 hour");
        }
    }
    
    @Scheduled(fixedRate = 21600000) // Every 6 hours
    public void refreshPopularTrending() {
        log.info("Refreshing popular trending songs (6-hour refresh)");
        
        // Refresh trending for top regions only
        List<String> topRegions = Arrays.asList("US", "UK", "CA");
        
        topRegions.forEach(region -> {
            try {
                musicRecommendationService.getTrendingSongs(region, null);
                log.debug("Refreshed trending for region: {}", region);
            } catch (Exception e) {
                log.error("Error refreshing trending for region {}: {}", region, e.getMessage());
            }
        });
    }
    
    @Scheduled(cron = "0 0 6 * * ?") // Every day at 6:00 AM
    public void morningWarmup() {
        log.info("Performing morning warmup - preloading most popular combinations");
        
        // Preload only the most popular combinations for faster morning response
        List<String> topGenres = Arrays.asList("pop", "rock", "hip-hop");
        List<String> topMoods = Arrays.asList("happy", "energetic", "chill");
        
        topGenres.forEach(genre -> 
            topMoods.forEach(mood -> {
                try {
                    RecommendationRequestDto request = RecommendationRequestDto.builder()
                            .genre(genre)
                            .mood(mood)
                            .limit(5)
                            .build();
                    
                    musicRecommendationService.getRecommendations(request);
                } catch (Exception e) {
                    log.error("Error during morning warmup for {} - {}: {}", genre, mood, e.getMessage());
                }
            })
        );
        
        log.info("Morning warmup completed");
    }
}