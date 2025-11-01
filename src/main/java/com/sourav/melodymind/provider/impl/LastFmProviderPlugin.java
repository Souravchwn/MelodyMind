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
public class LastFmProviderPlugin implements MusicProviderPlugin {
    
    private final ProviderConfig providerConfig;
    private final WebClient.Builder webClientBuilder;
    
    @Override
    public String getProviderName() {
        return ApplicationConstants.PROVIDER_LASTFM;
    }
    
    @Override
    public CompletableFuture<List<Song>> fetchRecommendations(RecommendationRequestDto request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching recommendations from LastFM for genre: {}, mood: {}", 
                    request.getGenre(), request.getMood());
            
            if (!isEnabled()) {
                log.warn("LastFM provider is disabled");
                return List.of();
            }
            
            try {
                // TODO: Replace with actual LastFM API calls
                // WebClient webClient = webClientBuilder
                //     .baseUrl(providerConfig.getLastfm().getBaseUrl())
                //     .build();
                
                // For now, return mock data
                return getMockLastFmData(request);
                
            } catch (Exception e) {
                log.error("Error fetching recommendations from LastFM", e);
                return List.of();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<Song>> fetchTrendingSongs(String region) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching trending songs from LastFM for region: {}", region);
            
            if (!isEnabled()) {
                log.warn("LastFM provider is disabled");
                return List.of();
            }
            
            try {
                // TODO: Replace with actual LastFM API calls
                return getMockTrendingData(region);
                
            } catch (Exception e) {
                log.error("Error fetching trending songs from LastFM", e);
                return List.of();
            }
        });
    }
    
    @Override
    public CompletableFuture<String> fetchLyrics(String artist, String title) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("LastFM has limited lyrics support for {} - {}", artist, title);
            return null; // Return null so dedicated lyrics service can handle it
        });
    }
    
    @Override
    public CompletableFuture<String> fetchPianoChords(String artist, String title) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("LastFM doesn't provide chords for {} - {}", artist, title);
            return null; // Return null so dedicated chords service can handle it
        });
    }
    
    @Override
    public boolean isEnabled() {
        return providerConfig.getLastfm().isEnabled();
    }
    
    @Override
    public int getPriority() {
        return 20; // Medium priority for LastFM
    }
    
    private List<Song> getMockLastFmData(RecommendationRequestDto request) {
        return List.of(
            Song.builder()
                .title("Bohemian Rhapsody")
                .artist("Queen")
                .genre(request.getGenre() != null ? request.getGenre() : "Rock")
                .language(request.getLanguage() != null ? request.getLanguage() : "en")
                .region(request.getRegion() != null ? request.getRegion() : "UK")
                .mood(request.getMood() != null ? request.getMood() : "dramatic")
                .previewUrl("https://lastfm-preview.example.com/bohemian-rhapsody")
                .provider("LastFM")
                .popularityScore(96.0)
                .externalId("lastfm:track:queen-bohemian-rhapsody")
                .fetchedAt(LocalDateTime.now())
                .build(),
            Song.builder()
                .title("Hotel California")
                .artist("Eagles")
                .genre(request.getGenre() != null ? request.getGenre() : "Rock")
                .language(request.getLanguage() != null ? request.getLanguage() : "en")
                .region(request.getRegion() != null ? request.getRegion() : "US")
                .mood(request.getMood() != null ? request.getMood() : "mellow")
                .previewUrl("https://lastfm-preview.example.com/hotel-california")
                .provider("LastFM")
                .popularityScore(94.0)
                .externalId("lastfm:track:eagles-hotel-california")
                .fetchedAt(LocalDateTime.now())
                .build()
        );
    }
    
    private List<Song> getMockTrendingData(String region) {
        return List.of(
            Song.builder()
                .title("As It Was")
                .artist("Harry Styles")
                .genre("Pop")
                .language("en")
                .region(region != null ? region : "UK")
                .mood("nostalgic")
                .previewUrl("https://lastfm-preview.example.com/as-it-was")
                .provider("LastFM")
                .popularityScore(93.0)
                .externalId("lastfm:track:harry-styles-as-it-was")
                .fetchedAt(LocalDateTime.now())
                .build()
        );
    }
}