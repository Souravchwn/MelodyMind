package com.sourav.melodymind.service.impl;

import com.sourav.melodymind.entity.Song;
import com.sourav.melodymind.repository.SongRepository;
import com.sourav.melodymind.service.DataInitializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationServiceImpl implements DataInitializationService, CommandLineRunner {
    
    private final SongRepository songRepository;
    
    @Override
    public void run(String... args) {
        if (songRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeSampleData();
            log.info("Sample data initialization completed");
        } else {
            log.info("Database already contains data, skipping initialization");
        }
    }
    
    @Override
    public void initializeSampleData() {
        List<Song> sampleSongs = Arrays.asList(
            Song.builder()
                .title("Blinding Lights")
                .artist("The Weeknd")
                .genre("Pop")
                .language("en")
                .region("US")
                .mood("energetic")
                .previewUrl("https://example.com/preview/blinding-lights.mp3")
                .provider("Spotify")
                .popularityScore(95.0)
                .externalId("spotify:track:0VjIjW4GlULA4LGoDOLVKN")
                .fetchedAt(LocalDateTime.now())
                .lyrics("I've been tryna call\nI've been on my own for long enough...")
                .pianoChords("Fm - Ab - Bb - Db\nVerse: Fm Ab Bb Db\nChorus: Ab Bb Fm Db")
                .build(),
                
            Song.builder()
                .title("Shape of You")
                .artist("Ed Sheeran")
                .genre("Pop")
                .language("en")
                .region("UK")
                .mood("happy")
                .previewUrl("https://example.com/preview/shape-of-you.mp3")
                .provider("Spotify")
                .popularityScore(92.0)
                .externalId("spotify:track:7qiZfU4dY1lWllzX7mPBI3")
                .fetchedAt(LocalDateTime.now())
                .lyrics("The club isn't the best place to find a lover...")
                .pianoChords("C#m - F#m - A - B\nVerse: C#m F#m A B\nChorus: A B C#m F#m")
                .build(),
                
            Song.builder()
                .title("Bohemian Rhapsody")
                .artist("Queen")
                .genre("Rock")
                .language("en")
                .region("UK")
                .mood("dramatic")
                .previewUrl("https://example.com/preview/bohemian-rhapsody.mp3")
                .provider("LastFM")
                .popularityScore(96.0)
                .externalId("lastfm:track:queen-bohemian-rhapsody")
                .fetchedAt(LocalDateTime.now())
                .lyrics("Is this the real life?\nIs this just fantasy?...")
                .pianoChords("Bb - F - Gm - Dm - Eb\nVerse: Bb F Gm Dm Eb\nChorus: F Bb Eb F")
                .build(),
                
            Song.builder()
                .title("Hotel California")
                .artist("Eagles")
                .genre("Rock")
                .language("en")
                .region("US")
                .mood("mellow")
                .previewUrl("https://example.com/preview/hotel-california.mp3")
                .provider("LastFM")
                .popularityScore(94.0)
                .externalId("lastfm:track:eagles-hotel-california")
                .fetchedAt(LocalDateTime.now())
                .lyrics("On a dark desert highway, cool wind in my hair...")
                .pianoChords("Bm - F# - A - E - G - D - Em - F#\nVerse: Bm F# A E G D Em F#")
                .build(),
                
            Song.builder()
                .title("Anti-Hero")
                .artist("Taylor Swift")
                .genre("Pop")
                .language("en")
                .region("US")
                .mood("introspective")
                .previewUrl("https://example.com/preview/anti-hero.mp3")
                .provider("Spotify")
                .popularityScore(98.0)
                .externalId("spotify:track:4Dvkj6JhhA12EX05fT7y2e")
                .fetchedAt(LocalDateTime.now())
                .lyrics("I have this thing where I get older but just never wiser...")
                .pianoChords("C - G - Am - F\nVerse: C G Am F\nChorus: F C G Am")
                .build()
        );
        
        songRepository.saveAll(sampleSongs);
        log.info("Saved {} sample songs", sampleSongs.size());
    }
}