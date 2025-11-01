package com.sourav.melodymind.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourav.melodymind.provider.config.ProviderConfig;
import com.sourav.melodymind.service.LyricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class LyricsServiceImpl implements LyricsService {
    
    private final WebClient.Builder webClientBuilder;
    private final ProviderConfig providerConfig;
    private final ObjectMapper objectMapper;
    
    @Override
    public CompletableFuture<String> fetchLyrics(String artist, String title) {
        log.info("Fetching lyrics for '{}' by '{}' on thread: {}", title, artist, Thread.currentThread().getName());
        
        // Try Lyrics.ovh first (free API)
        return fetchFromLyricsOvh(artist, title)
                .exceptionally(throwable -> {
                    log.warn("Lyrics.ovh failed for '{}' by '{}': {}", title, artist, throwable.getMessage());
                    return null;
                })
                .thenCompose(lyrics -> {
                    if (lyrics != null && !lyrics.trim().isEmpty()) {
                        return CompletableFuture.completedFuture(lyrics);
                    }
                    // Fallback to Genius API
                    return fetchFromGenius(artist, title)
                            .exceptionally(throwable -> {
                                log.warn("Genius API failed for '{}' by '{}': {}", title, artist, throwable.getMessage());
                                return null;
                            });
                })
                .thenApply(lyrics -> {
                    if (lyrics != null && !lyrics.trim().isEmpty()) {
                        log.info("Successfully fetched lyrics for '{}' by '{}' from real API", title, artist);
                        return lyrics;
                    } else {
                        log.warn("All real APIs failed for '{}' by '{}', using fallback mock data", title, artist);
                        return getMockLyrics(artist, title);
                    }
                });
    }
    
    @Override
    @Cacheable(value = "lyrics", key = "#artist.toLowerCase() + '_' + #title.toLowerCase()")
    public String getLyrics(String artist, String title) {
        log.debug("Getting lyrics for '{}' by '{}' (cached or fresh)", title, artist);
        return fetchLyrics(artist, title).join();
    }
    
    private CompletableFuture<String> fetchFromLyricsOvh(String artist, String title) {
        log.debug("Trying Lyrics.ovh API for '{}' by '{}'", title, artist);
        
        WebClient webClient = webClientBuilder
                .baseUrl(providerConfig.getLyrics().getLyricsOvhUrl())
                .build();
        
        return webClient.get()
                .uri("/{artist}/{title}", artist, title)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(providerConfig.getLyrics().getTimeout() / 1000))
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        String lyrics = jsonNode.path("lyrics").asText();
                        
                        if (lyrics != null && !lyrics.trim().isEmpty() && !lyrics.equals("null")) {
                            log.info("Successfully fetched lyrics from Lyrics.ovh for '{}' by '{}'", title, artist);
                            return lyrics.trim();
                        } else {
                            log.debug("Empty lyrics response from Lyrics.ovh for '{}' by '{}'", title, artist);
                            return null;
                        }
                    } catch (Exception e) {
                        log.error("Error parsing Lyrics.ovh response for '{}' by '{}': {}", title, artist, e.getMessage());
                        return null;
                    }
                })
                .onErrorMap(WebClientResponseException.class, ex -> {
                    log.error("Lyrics.ovh API error for '{}' by '{}': {} - {}", title, artist, ex.getStatusCode(), ex.getMessage());
                    return new RuntimeException("Lyrics.ovh API failed: " + ex.getMessage());
                })
                .toFuture();
    }
    
    private CompletableFuture<String> fetchFromGenius(String artist, String title) {
        log.debug("Trying Genius API for '{}' by '{}'", title, artist);
        
        if (providerConfig.getLyrics().getGeniusApiKey() == null || 
            providerConfig.getLyrics().getGeniusApiKey().startsWith("your_")) {
            log.warn("Genius API key not configured, skipping Genius API call");
            return CompletableFuture.completedFuture(null);
        }
        
        WebClient webClient = webClientBuilder
                .baseUrl(providerConfig.getLyrics().getGeniusBaseUrl())
                .defaultHeader("Authorization", "Bearer " + providerConfig.getLyrics().getGeniusApiKey())
                .build();
        
        // First, search for the song
        return webClient.get()
                .uri("/search?q={query}", artist + " " + title)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(providerConfig.getLyrics().getTimeout() / 1000))
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        JsonNode hits = jsonNode.path("response").path("hits");
                        
                        if (hits.isArray() && hits.size() > 0) {
                            JsonNode firstHit = hits.get(0);
                            String songUrl = firstHit.path("result").path("url").asText();
                            
                            if (songUrl != null && !songUrl.isEmpty()) {
                                log.info("Found song on Genius for '{}' by '{}': {}", title, artist, songUrl);
                                // Note: Genius doesn't provide lyrics directly via API due to licensing
                                // This would require web scraping which is against their ToS
                                return "Lyrics found on Genius: " + songUrl + "\n\n" +
                                       "Note: Full lyrics require visiting the Genius website due to licensing restrictions.";
                            }
                        }
                        
                        log.debug("No results found on Genius for '{}' by '{}'", title, artist);
                        return null;
                    } catch (Exception e) {
                        log.error("Error parsing Genius response for '{}' by '{}': {}", title, artist, e.getMessage());
                        return null;
                    }
                })
                .onErrorMap(WebClientResponseException.class, ex -> {
                    log.error("Genius API error for '{}' by '{}': {} - {}", title, artist, ex.getStatusCode(), ex.getMessage());
                    return new RuntimeException("Genius API failed: " + ex.getMessage());
                })
                .toFuture();
    }
    
    private String getMockLyrics(String artist, String title) {
        log.info("Generating mock lyrics for '{}' by '{}' as fallback", title, artist);
        
        return String.format("""
                [Verse 1]
                This is a sample lyric for "%s"
                Performed by the amazing %s
                These are placeholder lyrics since the real APIs failed
                But the music still plays in our hearts
                
                [Chorus]
                %s, %s, singing so bright
                Music brings joy both day and night
                Even without the real lyrics here
                The melody makes everything clear
                
                [Verse 2]
                In a world of music and sound
                Great artists like %s can be found
                "%s" is a song that we love
                Sent to us from the heavens above
                
                [Outro]
                This is a fallback lyric display
                Real lyrics will come another day
                For now enjoy this placeholder text
                While we work on getting the lyrics next
                
                ---
                Note: These are placeholder lyrics. Real lyrics could not be fetched from external APIs.
                """, title, artist, title, artist, artist, title);
    }
}