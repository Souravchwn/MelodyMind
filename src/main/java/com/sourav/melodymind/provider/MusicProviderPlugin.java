package com.sourav.melodymind.provider;

import com.sourav.melodymind.dto.RecommendationRequestDto;
import com.sourav.melodymind.entity.Song;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for music provider plugins.
 * Each provider (Spotify, LastFM, Deezer, etc.) should implement this interface.
 */
public interface MusicProviderPlugin {
    
    /**
     * Get the name of this provider
     * @return provider name
     */
    String getProviderName();
    
    /**
     * Fetch music recommendations based on the given criteria
     * @param request recommendation request with filters
     * @return CompletableFuture containing list of songs
     */
    CompletableFuture<List<Song>> fetchRecommendations(RecommendationRequestDto request);
    
    /**
     * Fetch trending songs for a specific region
     * @param region region code (e.g., "US", "UK")
     * @return CompletableFuture containing list of trending songs
     */
    CompletableFuture<List<Song>> fetchTrendingSongs(String region);
    
    /**
     * Fetch lyrics for a specific song
     * @param artist artist name
     * @param title song title
     * @return CompletableFuture containing lyrics text
     */
    CompletableFuture<String> fetchLyrics(String artist, String title);
    
    /**
     * Fetch piano chords for a specific song
     * @param artist artist name
     * @param title song title
     * @return CompletableFuture containing piano chords
     */
    CompletableFuture<String> fetchPianoChords(String artist, String title);
    
    /**
     * Check if this provider is enabled
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();
    
    /**
     * Get the priority of this provider (lower number = higher priority)
     * @return priority value
     */
    default int getPriority() {
        return 100;
    }
}