package com.sourav.melodymind.provider.impl;

import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.RecommendationRequestDto;
import com.sourav.melodymind.entity.Song;
import com.sourav.melodymind.provider.MusicProviderPlugin;
import com.sourav.melodymind.provider.config.ProviderConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpotifyProviderPlugin implements MusicProviderPlugin {
    
    private final ProviderConfig providerConfig;
    private final WebClient.Builder webClientBuilder;
    
    @Override
    public String getProviderName() {
        return ApplicationConstants.PROVIDER_SPOTIFY;
    }
    
    @Override
    public CompletableFuture<List<Song>> fetchRecommendations(RecommendationRequestDto request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching recommendations from Spotify for genre: {}, mood: {}", 
                    request.getGenre(), request.getMood());
            
            if (!isEnabled()) {
                log.warn("Spotify provider is disabled");
                return List.of();
            }
            
            try {
                // TODO: Replace with actual Spotify API calls
                // WebClient webClient = webClientBuilder
                //     .baseUrl(providerConfig.getSpotify().getBaseUrl())
                //     .build();
                
                // For now, return mock data
                return getMockSpotifyData(request);
                
            } catch (Exception e) {
                log.error("Error fetching recommendations from Spotify", e);
                return List.of();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<Song>> fetchTrendingSongs(String region) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching trending songs from Spotify for region: {}", region);
            
            if (!isEnabled()) {
                log.warn("Spotify provider is disabled");
                return List.of();
            }
            
            try {
                // TODO: Replace with actual Spotify API calls
                return getMockTrendingData(region);
                
            } catch (Exception e) {
                log.error("Error fetching trending songs from Spotify", e);
                return List.of();
            }
        });
    }
    
    @Override
    public CompletableFuture<String> fetchLyrics(String artist, String title) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Spotify doesn't provide lyrics directly for {} - {}", artist, title);
            return null; // Return null so other providers can be tried
        });
    }
    
    @Override
    public CompletableFuture<String> fetchPianoChords(String artist, String title) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Spotify doesn't provide chords directly for {} - {}", artist, title);
            return null; // Return null so other providers can be tried
        });
    }
    
    @Override
    public boolean isEnabled() {
        return providerConfig.getSpotify().isEnabled();
    }
    
    @Override
    public int getPriority() {
        return 10; // High priority for Spotify
    }
    
    private List<Song> getMockSpotifyData(RecommendationRequestDto request) {
        return List.of(
            Song.builder()
                .title("Blinding Lights")
                .artist("The Weeknd")
                .genre(request.getGenre() != null ? request.getGenre() : "Pop")
                .language(request.getLanguage() != null ? request.getLanguage() : ApplicationConstants.DEFAULT_LANGUAGE)
                .region(request.getRegion() != null ? request.getRegion() : ApplicationConstants.DEFAULT_REGION)
                .mood(request.getMood() != null ? request.getMood() : "energetic")
                .previewUrl("https://p.scdn.co/mp3-preview/example")
                .provider(ApplicationConstants.PROVIDER_SPOTIFY)
                .popularityScore(95.0)
                .externalId("spotify:track:0VjIjW4GlULA4LGoDOLVKN")
                .fetchedAt(LocalDateTime.now())
                .build(),
            Song.builder()
                .title("Shape of You")
                .artist("Ed Sheeran")
                .genre(request.getGenre() != null ? request.getGenre() : "Pop")
                .language(request.getLanguage() != null ? request.getLanguage() : ApplicationConstants.DEFAULT_LANGUAGE)
                .region(request.getRegion() != null ? request.getRegion() : ApplicationConstants.DEFAULT_REGION)
                .mood(request.getMood() != null ? request.getMood() : "happy")
                .previewUrl("https://p.scdn.co/mp3-preview/example2")
                .provider(ApplicationConstants.PROVIDER_SPOTIFY)
                .popularityScore(92.0)
                .externalId("spotify:track:7qiZfU4dY1lWllzX7mPBI3")
                .fetchedAt(LocalDateTime.now())
                .build()
        );
    }
    
    private List<Song> getMockTrendingData(String region) {
        return List.of(
            Song.builder()
                .title("Anti-Hero")
                .artist("Taylor Swift")
                .genre("Pop")
                .language(ApplicationConstants.DEFAULT_LANGUAGE)
                .region(region != null ? region : ApplicationConstants.DEFAULT_REGION)
                .mood("introspective")
                .previewUrl("https://p.scdn.co/mp3-preview/trending1")
                .provider(ApplicationConstants.PROVIDER_SPOTIFY)
                .popularityScore(98.0)
                .externalId("spotify:track:4Dvkj6JhhA12EX05fT7y2e")
                .fetchedAt(LocalDateTime.now())
                .build()
        );
    }
}