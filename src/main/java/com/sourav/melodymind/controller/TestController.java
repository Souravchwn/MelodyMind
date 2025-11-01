package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.ApiResponse;
import com.sourav.melodymind.service.ChordsService;
import com.sourav.melodymind.service.LyricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(ApiConstants.TEST_FULL_PATH)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TestController {
    
    private final LyricsService lyricsService;
    private final ChordsService chordsService;
    
    @GetMapping(ApiConstants.TEST_LYRICS)
    public ResponseEntity<ApiResponse<String>> testLyrics(
            @RequestParam(ApiConstants.PARAM_ARTIST) String artist,
            @RequestParam(ApiConstants.PARAM_TITLE) String title) {
        
        log.info("Testing lyrics API for '{}' by '{}' on thread: {}", title, artist, Thread.currentThread().getName());
        
        String lyrics = lyricsService.getLyrics(artist, title);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_LYRICS_TEST_COMPLETED, 
                lyrics
        ));
    }
    
    @GetMapping(ApiConstants.TEST_CHORDS)
    public ResponseEntity<ApiResponse<String>> testChords(
            @RequestParam(ApiConstants.PARAM_ARTIST) String artist,
            @RequestParam(ApiConstants.PARAM_TITLE) String title) {
        
        log.info("Testing chords API for '{}' by '{}' on thread: {}", title, artist, Thread.currentThread().getName());
        
        String chords = chordsService.getPianoChords(artist, title);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_CHORDS_TEST_COMPLETED, 
                chords
        ));
    }
    
    @GetMapping(ApiConstants.TEST_ASYNC)
    public ResponseEntity<ApiResponse<Map<String, String>>> testAsyncBoth(
            @RequestParam(ApiConstants.PARAM_ARTIST) String artist,
            @RequestParam(ApiConstants.PARAM_TITLE) String title) {
        
        log.info("Testing async lyrics and chords for '{}' by '{}' on thread: {}", 
                title, artist, Thread.currentThread().getName());
        
        // Test concurrent fetching
        CompletableFuture<String> lyricsAsync = lyricsService.fetchLyrics(artist, title);
        CompletableFuture<String> chordsAsync = chordsService.fetchPianoChords(artist, title);
        
        // Wait for both to complete
        CompletableFuture<Map<String, String>> combined = lyricsAsync.thenCombine(chordsAsync, 
                (lyrics, chords) -> Map.of(
                        "lyrics", lyrics,
                        "chords", chords
                ));
        
        Map<String, String> result = combined.join();
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_ASYNC_TEST_COMPLETED, 
                result
        ));
    }
    
    @GetMapping(ApiConstants.TEST_THREAD_INFO)
    public ResponseEntity<ApiResponse<Map<String, Object>>> getThreadInfo() {
        
        Thread currentThread = Thread.currentThread();
        
        Map<String, Object> threadInfo = Map.of(
                "threadName", currentThread.getName(),
                "threadId", currentThread.getId(),
                "threadGroup", currentThread.getThreadGroup().getName(),
                "isVirtual", currentThread.isVirtual(),
                "activeThreadCount", Thread.activeCount(),
                "timestamp", System.currentTimeMillis()
        );
        
        log.info("Thread info requested on thread: {} (ID: {}, Virtual: {})", 
                currentThread.getName(), currentThread.getId(), currentThread.isVirtual());
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_THREAD_INFO_RETRIEVED, 
                threadInfo
        ));
    }
}