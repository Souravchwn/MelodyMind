package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.ApiResponse;
import com.sourav.melodymind.service.SchedulerService;
import com.sourav.melodymind.service.SchedulerStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.SCHEDULER_FULL_PATH)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SchedulerController {
    
    private final SchedulerService schedulerService;
    
    @GetMapping(ApiConstants.SCHEDULER_STATS)
    public ResponseEntity<ApiResponse<SchedulerStats>> getSchedulerStats() {
        log.info("Fetching scheduler statistics");
        
        SchedulerStats stats = schedulerService.getSchedulerStats();
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_SCHEDULER_STATS_FETCHED, 
                stats
        ));
    }
    
    @PostMapping(ApiConstants.SCHEDULER_CLEANUP_MANUAL)
    public ResponseEntity<ApiResponse<String>> triggerManualCleanup() {
        log.info("Manual cleanup triggered");
        
        try {
            schedulerService.performDailyCleanup();
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_MANUAL_CLEANUP_COMPLETED, 
                    ApplicationConstants.MSG_SUCCESS
            ));
        } catch (Exception e) {
            log.error("Error during manual cleanup", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(ApplicationConstants.ERR_MANUAL_CLEANUP_FAILED + ": " + e.getMessage()));
        }
    }
    
    @PostMapping(ApiConstants.SCHEDULER_TRENDING_REFRESH)
    public ResponseEntity<ApiResponse<String>> refreshTrending() {
        log.info("Manual trending refresh triggered");
        
        try {
            schedulerService.refreshTrendingSongs();
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_TRENDING_REFRESH_COMPLETED, 
                    ApplicationConstants.MSG_SUCCESS
            ));
        } catch (Exception e) {
            log.error("Error during manual trending refresh", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(ApplicationConstants.ERR_TRENDING_REFRESH_FAILED + ": " + e.getMessage()));
        }
    }
    
    @PostMapping(ApiConstants.SCHEDULER_CACHE_CLEAR)
    public ResponseEntity<ApiResponse<String>> clearCache() {
        log.info("Manual cache clear triggered");
        
        try {
            schedulerService.clearExpiredCache();
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_CACHE_CLEARED, 
                    ApplicationConstants.MSG_SUCCESS
            ));
        } catch (Exception e) {
            log.error("Error during manual cache clear", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(ApplicationConstants.ERR_CACHE_CLEAR_FAILED + ": " + e.getMessage()));
        }
    }
    
    @PostMapping(ApiConstants.SCHEDULER_PRELOAD)
    public ResponseEntity<ApiResponse<String>> preloadRecommendations() {
        log.info("Manual preload triggered");
        
        try {
            schedulerService.preloadPopularRecommendations();
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_PRELOAD_COMPLETED, 
                    ApplicationConstants.MSG_SUCCESS
            ));
        } catch (Exception e) {
            log.error("Error during manual preload", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(ApplicationConstants.ERR_PRELOAD_FAILED + ": " + e.getMessage()));
        }
    }
}