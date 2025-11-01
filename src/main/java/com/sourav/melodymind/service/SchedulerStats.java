package com.sourav.melodymind.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerStats {
    
    private LocalDateTime lastCleanupTime;
    private LocalDateTime lastTrendingRefresh;
    private int totalCleanups;
    private int totalTrendingRefreshes;
    private long totalSongsCleanedUp;
    private int enabledProviders;
    private Collection<String> cacheNames;
    private String status;
    
    public String getStatus() {
        if (lastCleanupTime == null) {
            return "NEVER_RUN";
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        
        if (lastCleanupTime.isAfter(yesterday)) {
            return "HEALTHY";
        } else {
            return "OVERDUE";
        }
    }
}