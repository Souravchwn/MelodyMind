package com.sourav.melodymind.service;

import java.util.concurrent.CompletableFuture;

public interface ChordsService {
    
    /**
     * Fetch piano chords for a song from multiple providers
     * @param artist artist name
     * @param title song title
     * @return CompletableFuture containing piano chords
     */
    CompletableFuture<String> fetchPianoChords(String artist, String title);
    
    /**
     * Get cached chords or fetch from providers
     * @param artist artist name
     * @param title song title
     * @return piano chords text
     */
    String getPianoChords(String artist, String title);
}