package com.sourav.melodymind.service.impl;

import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.RecommendationRequestDto;
import com.sourav.melodymind.dto.SongDto;
import com.sourav.melodymind.entity.Song;
import com.sourav.melodymind.provider.MusicProviderPlugin;
import com.sourav.melodymind.repository.SongRepository;
import com.sourav.melodymind.repository.UserFavoriteRepository;
import com.sourav.melodymind.service.MusicRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicRecommendationServiceImpl implements MusicRecommendationService {
    
    private final List<MusicProviderPlugin> providers;
    private final SongRepository songRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final com.sourav.melodymind.service.LyricsService lyricsService;
    private final com.sourav.melodymind.service.ChordsService chordsService;
    
    @Override
    @Cacheable(value = ApplicationConstants.CACHE_RECOMMENDATIONS, key = "#request.genre + '_' + #request.language + '_' + #request.region + '_' + #request.mood + '_' + #request.limit")
    public List<SongDto> getRecommendations(RecommendationRequestDto request) {
        log.info("Fetching recommendations for request: {}", request);
        
        // First, try to get from database
        List<Song> cachedSongs = songRepository.findRecommendations(
            request.getGenre(), 
            request.getLanguage(), 
            request.getRegion(), 
            request.getMood()
        );
        
        if (!cachedSongs.isEmpty()) {
            log.info("Found {} cached songs", cachedSongs.size());
            return convertToDto(cachedSongs, request.getUserId())
                    .stream()
                    .limit(request.getLimit())
                    .collect(Collectors.toList());
        }
        
        // If no cached results, fetch from providers concurrently
        List<CompletableFuture<List<Song>>> futures = providers.stream()
                .filter(MusicProviderPlugin::isEnabled)
                .map(provider -> provider.fetchRecommendations(request)
                        .exceptionally(throwable -> {
                            log.error("Error fetching from provider {}: {}", 
                                    provider.getProviderName(), throwable.getMessage());
                            return Collections.emptyList();
                        }))
                .collect(Collectors.toList());
        
        // Wait for all providers to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        
        List<Song> allSongs = allOf.thenApply(v -> 
                futures.stream()
                        .flatMap(future -> future.join().stream())
                        .collect(Collectors.toList())
        ).join();
        
        // Deduplicate and save to database
        List<Song> uniqueSongs = deduplicateAndSave(allSongs);
        
        return convertToDto(uniqueSongs, request.getUserId())
                .stream()
                .limit(request.getLimit())
                .collect(Collectors.toList());
    }
    
    @Override
    @Cacheable(value = ApplicationConstants.CACHE_TRENDING, key = "#region + '_' + (#userId != null ? #userId : 'anonymous')")
    public List<SongDto> getTrendingSongs(String region, Long userId) {
        log.info("Fetching trending songs for region: {}", region);
        
        List<CompletableFuture<List<Song>>> futures = providers.stream()
                .filter(MusicProviderPlugin::isEnabled)
                .map(provider -> provider.fetchTrendingSongs(region)
                        .exceptionally(throwable -> {
                            log.error("Error fetching trending from provider {}: {}", 
                                    provider.getProviderName(), throwable.getMessage());
                            return Collections.emptyList();
                        }))
                .collect(Collectors.toList());
        
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        
        List<Song> allSongs = allOf.thenApply(v -> 
                futures.stream()
                        .flatMap(future -> future.join().stream())
                        .collect(Collectors.toList())
        ).join();
        
        List<Song> uniqueSongs = deduplicateAndSave(allSongs);
        
        return convertToDto(uniqueSongs, userId)
                .stream()
                .limit(20)
                .collect(Collectors.toList());
    }
    
    @Override
    public String getLyrics(String artist, String title) {
        log.info("Fetching lyrics for {} - {} on thread: {}", artist, title, Thread.currentThread().getName());
        
        // Try to get from database first
        Optional<Song> existingSong = songRepository.findByTitleAndArtist(title, artist);
        if (existingSong.isPresent() && existingSong.get().getLyrics() != null && 
            !existingSong.get().getLyrics().trim().isEmpty()) {
            log.debug("Found cached lyrics in database for {} - {}", artist, title);
            return existingSong.get().getLyrics();
        }
        
        // Use dedicated lyrics service
        String lyrics = lyricsService.getLyrics(artist, title);
        
        // Update database if song exists
        existingSong.ifPresent(song -> {
            song.setLyrics(lyrics);
            songRepository.save(song);
            log.debug("Updated lyrics in database for {} - {}", artist, title);
        });
        
        return lyrics;
    }
    
    @Override
    public String getPianoChords(String artist, String title) {
        log.info("Fetching piano chords for {} - {} on thread: {}", artist, title, Thread.currentThread().getName());
        
        // Try to get from database first
        Optional<Song> existingSong = songRepository.findByTitleAndArtist(title, artist);
        if (existingSong.isPresent() && existingSong.get().getPianoChords() != null && 
            !existingSong.get().getPianoChords().trim().isEmpty()) {
            log.debug("Found cached chords in database for {} - {}", artist, title);
            return existingSong.get().getPianoChords();
        }
        
        // Use dedicated chords service
        String chords = chordsService.getPianoChords(artist, title);
        
        // Update database if song exists
        existingSong.ifPresent(song -> {
            song.setPianoChords(chords);
            songRepository.save(song);
            log.debug("Updated chords in database for {} - {}", artist, title);
        });
        
        return chords;
    }
    
    private List<Song> deduplicateAndSave(List<Song> songs) {
        Map<String, Song> uniqueSongs = new LinkedHashMap<>();
        
        for (Song song : songs) {
            String key = song.getTitle().toLowerCase() + "_" + song.getArtist().toLowerCase();
            if (!uniqueSongs.containsKey(key)) {
                // Check if song already exists in database
                Optional<Song> existing = songRepository.findByTitleAndArtist(song.getTitle(), song.getArtist());
                if (existing.isPresent()) {
                    uniqueSongs.put(key, existing.get());
                } else {
                    Song saved = songRepository.save(song);
                    uniqueSongs.put(key, saved);
                }
            }
        }
        
        return new ArrayList<>(uniqueSongs.values());
    }
    
    private List<SongDto> convertToDto(List<Song> songs, Long userId) {
        Set<Long> favoriteSongIds = Collections.emptySet();
        
        if (userId != null) {
            favoriteSongIds = userFavoriteRepository.findByUserIdOrderByAddedAtDesc(userId)
                    .stream()
                    .map(fav -> fav.getSongId())
                    .collect(Collectors.toSet());
        }
        
        final Set<Long> finalFavoriteSongIds = favoriteSongIds;
        
        return songs.stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .title(song.getTitle())
                        .artist(song.getArtist())
                        .genre(song.getGenre())
                        .language(song.getLanguage())
                        .region(song.getRegion())
                        .previewUrl(song.getPreviewUrl())
                        .lyrics(song.getLyrics())
                        .pianoChords(song.getPianoChords())
                        .fetchedAt(song.getFetchedAt())
                        .provider(song.getProvider())
                        .popularityScore(song.getPopularityScore())
                        .mood(song.getMood())
                        .isFavorite(finalFavoriteSongIds.contains(song.getId()))
                        .build())
                .collect(Collectors.toList());
    }
}