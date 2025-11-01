package com.sourav.melodymind.service;

import com.sourav.melodymind.dto.RecommendationRequestDto;
import com.sourav.melodymind.dto.SongDto;

import java.util.List;

public interface MusicRecommendationService {
    
    List<SongDto> getRecommendations(RecommendationRequestDto request);
    
    List<SongDto> getTrendingSongs(String region, Long userId);
    
    String getLyrics(String artist, String title);
    
    String getPianoChords(String artist, String title);
}