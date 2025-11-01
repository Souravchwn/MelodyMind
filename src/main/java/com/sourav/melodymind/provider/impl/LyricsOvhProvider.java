package com.sourav.melodymind.provider.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourav.melodymind.provider.config.ProviderConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Real lyrics provider using Lyrics.ovh API
 * This is a free API that provides lyrics for songs
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LyricsOvhProvider {
    
    private final WebClient.Builder webClientBuilder;
    private final ProviderConfig providerConfig;
    private final ObjectMapper objectMapper;
    
    public CompletableFuture<String> fetchLyrics(String artist, String title) {
        log.info("Fetching lyrics from Lyrics.ovh for '{}' by '{}' on thread: {}", 
                title, artist, Thread.currentThread().getName());
        
        WebClient webClient = webClientBuilder
                .baseUrl("https://api.lyrics.ovh")
                .build();
        
        return webClient.get()
                .uri("/v1/{artist}/{title}", artist, title)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        String lyrics = jsonNode.path("lyrics").asText();
                        
                        if (lyrics != null && !lyrics.trim().isEmpty() && !lyrics.equals("null")) {
                            log.info("Successfully fetched lyrics from Lyrics.ovh for '{}' by '{}'", title, artist);
                            return lyrics.trim();
                        } else {
                            log.warn("Empty lyrics response from Lyrics.ovh for '{}' by '{}'", title, artist);
                            return null;
                        }
                    } catch (Exception e) {
                        log.error("Error parsing Lyrics.ovh response for '{}' by '{}': {}", title, artist, e.getMessage());
                        return null;
                    }
                })
                .onErrorMap(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().value() == 404) {
                        log.warn("Lyrics not found on Lyrics.ovh for '{}' by '{}'", title, artist);
                    } else {
                        log.error("Lyrics.ovh API error for '{}' by '{}': {} - {}", 
                                title, artist, ex.getStatusCode(), ex.getMessage());
                    }
                    return new RuntimeException("Lyrics.ovh API failed: " + ex.getMessage());
                })
                .toFuture();
    }
}