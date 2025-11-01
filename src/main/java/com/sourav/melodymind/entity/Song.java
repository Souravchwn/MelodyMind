package com.sourav.melodymind.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String artist;
    
    private String genre;
    
    private String language;
    
    private String region;
    
    @Column(name = "preview_url")
    private String previewUrl;
    
    @Column(columnDefinition = "TEXT")
    private String lyrics;
    
    @Column(name = "piano_chords", columnDefinition = "TEXT")
    private String pianoChords;
    
    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;
    
    @Column(name = "external_id")
    private String externalId;
    
    @Column(name = "provider")
    private String provider;
    
    @Column(name = "popularity_score")
    private Double popularityScore;
    
    @Column(name = "mood")
    private String mood;
    
    @PrePersist
    protected void onCreate() {
        fetchedAt = LocalDateTime.now();
    }
}