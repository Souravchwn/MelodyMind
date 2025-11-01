package com.sourav.melodymind.provider.impl;

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
public class DeezerProviderPlugin implements MusicProviderPlugin {
    
    private final ProviderConfig providerConfig;
    private final WebClient.Builder webClientBuilder;
    
    @Override
    public String getProviderName() {
        return "Deezer";
    }
    
    @Override
    public CompletableFuture<List<Song>> fetchRecommendations(RecommendationRequestDto request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching recommendations from Deezer for genre: {}, mood: {}", 
                    request.getGenre(), request.getMood());
            
            if (!isEnabled()) {
                log.warn("Deezer provider is disabled");
                return List.of();
            }
            
            try {
                // TODO: Replace with actual Deezer API calls
                // WebClient webClient = webClientBuilder
                //     .baseUrl(providerConfig.getDeezer().getBaseUrl())
                //     .build();
                
                // For now, return mock data
                return getMockDeezerData(request);
                
            } catch (Exception e) {
                log.error("Error fetching recommendations from Deezer", e);
                return List.of();
            }
        });
    }
    
    @Override
    public CompletableFuture<List<Song>> fetchTrendingSongs(String region) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching trending songs from Deezer for region: {}", region);
            
            if (!isEnabled()) {
                log.warn("Deezer provider is disabled");
                return List.of();
            }
            
            try {
                // TODO: Replace with actual Deezer API calls
                return getMockTrendingData(region);
                
            } catch (Exception e) {
                log.error("Error fetching trending songs from Deezer", e);
                return List.of();
            }
        });
    }
    
    @Override
    public CompletableFuture<String> fetchLyrics(String artist, String title) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Deezer has limited lyrics support for {} - {}", artist, title);
            return null; // Return null so dedicated lyrics service can handle it
        });
    }
    
    @Override
    public CompletableFuture<String> fetchPianoChords(String artist, String title) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Deezer doesn't provide chords for {} - {}", artist, title);
            return null; // Return null so dedicated chords service can handle it
        });
    }
    
    @Override
    public boolean isEnabled() {
        return providerConfig.getDeezer().isEnabled();
    }
    
    @Override
    public int getPriority() {
        return 30; // Lower priority for Deezer
    }
    
    private List<Song> getMockDeezerData(RecommendationRequestDto request) {
        return List.of(
            Song.builder()
                .title("Watermelon Sugar")
                .artist("Harry Styles")
                .genre(request.getGenre() != null ? request.getGenre() : "Pop")
                .language(request.getLanguage() != null ? request.getLanguage() : "en")
                .region(request.getRegion() != null ? request.getRegion() : "UK")
                .mood(request.getMood() != null ? request.getMood() : "happy")
                .previewUrl("https://cdns-preview-d.dzcdn.net/stream/example")
                .provider("Deezer")
                .popularityScore(89.0)
                .externalId("deezer:track:123456789")
                .fetchedAt(LocalDateTime.now())
                .build(),
            Song.builder()
                .title("Good 4 U")
                .artist("Olivia Rodrigo")
                .genre(request.getGenre() != null ? request.getGenre() : "Pop")
                .language(request.getLanguage() != null ? request.getLanguage() : "en")
                .region(request.getRegion() != null ? request.getRegion() : "US")
                .mood(request.getMood() != null ? request.getMood() : "energetic")
                .previewUrl("https://cdns-preview-d.dzcdn.net/stream/example2")
                .provider("Deezer")
                .popularityScore(91.0)
                .externalId("deezer:track:987654321")
                .fetchedAt(LocalDateTime.now())
                .build()
        );
    }
    
    private List<Song> getMockTrendingData(String region) {
        return List.of(
            Song.builder()
                .title("Flowers")
                .artist("Miley Cyrus")
                .genre("Pop")
                .language("en")
                .region(region != null ? region : "US")
                .mood("empowering")
                .previewUrl("https://cdns-preview-d.dzcdn.net/stream/flowers")
                .provider("Deezer")
                .popularityScore(97.0)
                .externalId("deezer:track:flowers123")
                .fetchedAt(LocalDateTime.now())
                .build()
        );
    }
}