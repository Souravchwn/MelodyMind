package com.sourav.melodymind.service;

import java.util.concurrent.CompletableFuture;

public interface LyricsService {
    
    /**
     * Fetch lyrics for a song from multiple providers
     * @param artist artist name
     * @param title song title
     * @return CompletableFuture containing lyrics text
     */
    CompletableFuture<String> fetchLyrics(String artist, String title);
    
    /**
     * Get cached lyrics or fetch from providers
     * @param artist artist name
     * @param title song title
     * @return lyrics text
     */
    String getLyrics(String artist, String title);
}