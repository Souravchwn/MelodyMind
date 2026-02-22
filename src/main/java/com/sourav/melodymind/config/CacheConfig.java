package com.sourav.melodymind.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.registerCustomCache("recommendations", 
            Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.HOURS) // User requested songs - 1 hour
                .recordStats()
                .build());
        
        cacheManager.registerCustomCache("trending", 
            Caffeine.newBuilder()
                .initialCapacity(20)
                .maximumSize(200)
                .expireAfterWrite(24, TimeUnit.HOURS) // Trending songs - 24 hours
                .recordStats()
                .build());
        
        cacheManager.registerCustomCache("lyrics", 
            Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(7, TimeUnit.DAYS) // Lyrics - 7 days
                .recordStats()
                .build());
        
        cacheManager.registerCustomCache("chords", 
            Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(7, TimeUnit.DAYS) // Chords - 7 days
                .recordStats()
                .build());
        
        // Default cache for other operations
        cacheManager.setCaffeine(defaultCacheBuilder());
        
        return cacheManager;
    }
    
    private Caffeine<Object, Object> defaultCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats();
    }
}