package com.sourav.melodymind.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SongDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("artist")
    private String artist;
    
    @JsonProperty("genre")
    private String genre;
    
    @JsonProperty("language")
    private String language;
    
    @JsonProperty("region")
    private String region;
    
    @JsonProperty("preview_url")
    private String previewUrl;
    
    @JsonProperty("lyrics")
    private String lyrics;
    
    @JsonProperty("piano_chords")
    private String pianoChords;
    
    @JsonProperty("fetched_at")
    private LocalDateTime fetchedAt;
    
    @JsonProperty("provider")
    private String provider;
    
    @JsonProperty("popularity_score")
    private Double popularityScore;
    
    @JsonProperty("mood")
    private String mood;
    
    @JsonProperty("is_favorite")
    private boolean isFavorite;
}