package com.sourav.melodymind.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "melodymind")
public class MelodyMindProperties {
    
    private CacheConfig cache = new CacheConfig();
    private ApiConfig api = new ApiConfig();
    
    @Data
    public static class CacheConfig {
        private long refreshInterval = 300000; // 5 minutes
        private int maxSize = 1000;
        private long expireAfterWrite = 1800; // 30 minutes in seconds
    }
    
    @Data
    public static class ApiConfig {
        private int timeout = 5000; // 5 seconds
        private int retryAttempts = 3;
        private int maxConcurrentRequests = 100;
    }
}