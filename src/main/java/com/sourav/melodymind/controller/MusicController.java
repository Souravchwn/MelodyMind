package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.ApiResponse;
import com.sourav.melodymind.dto.RecommendationRequestDto;
import com.sourav.melodymind.dto.SongDto;
import com.sourav.melodymind.service.MusicRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.MUSIC_FULL_PATH)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MusicController {
    
    private final MusicRecommendationService musicRecommendationService;
    
    @GetMapping(ApiConstants.MUSIC_RECOMMENDATIONS)
    public ResponseEntity<ApiResponse<List<SongDto>>> getRecommendations(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String mood,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(required = false) Long userId) {
        
        log.info("Received recommendation request - genre: {}, language: {}, region: {}, mood: {}, limit: {}, userId: {}", 
                genre, language, region, mood, limit, userId);
        
        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .genre(genre)
                .language(language)
                .region(region)
                .mood(mood)
                .limit(limit)
                .userId(userId)
                .build();
        
        List<SongDto> recommendations = musicRecommendationService.getRecommendations(request);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_RECOMMENDATIONS_FETCHED, 
                recommendations
        ));
    }
    
    @GetMapping(ApiConstants.MUSIC_TRENDING)
    public ResponseEntity<ApiResponse<List<SongDto>>> getTrendingSongs(
            @RequestParam(defaultValue = ApplicationConstants.DEFAULT_REGION) String region,
            @RequestParam(required = false) Long userId) {
        
        log.info("Received trending request - region: {}, userId: {}", region, userId);
        
        List<SongDto> trendingSongs = musicRecommendationService.getTrendingSongs(region, userId);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_TRENDING_FETCHED, 
                trendingSongs
        ));
    }
    
    @GetMapping(ApiConstants.MUSIC_LYRICS)
    public ResponseEntity<ApiResponse<String>> getLyrics(
            @RequestParam(ApiConstants.PARAM_ARTIST) String artist,
            @RequestParam(ApiConstants.PARAM_TITLE) String title) {
        
        log.info("Received lyrics request - artist: {}, title: {}", artist, title);
        
        String lyrics = musicRecommendationService.getLyrics(artist, title);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_LYRICS_FETCHED, 
                lyrics
        ));
    }
    
    @GetMapping(ApiConstants.MUSIC_CHORDS)
    public ResponseEntity<ApiResponse<String>> getPianoChords(
            @RequestParam(ApiConstants.PARAM_ARTIST) String artist,
            @RequestParam(ApiConstants.PARAM_TITLE) String title) {
        
        log.info("Received chords request - artist: {}, title: {}", artist, title);
        
        String chords = musicRecommendationService.getPianoChords(artist, title);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_CHORDS_FETCHED, 
                chords
        ));
    }
}