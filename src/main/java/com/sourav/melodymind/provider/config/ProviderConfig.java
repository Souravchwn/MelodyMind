package com.sourav.melodymind.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "melodymind.providers")
public class ProviderConfig {
    
    private SpotifyConfig spotify = new SpotifyConfig();
    private LastFmConfig lastfm = new LastFmConfig();
    private DeezerConfig deezer = new DeezerConfig();
    private LyricsConfig lyrics = new LyricsConfig();
    
    @Data
    public static class SpotifyConfig {
        private boolean enabled = true;
        private String clientId = "your_spotify_client_id";
        private String clientSecret = "your_spotify_client_secret";
        private String baseUrl = "https://api.spotify.com/v1";
        private int timeout = 5000;
        private int retryAttempts = 3;
    }
    
    @Data
    public static class LastFmConfig {
        private boolean enabled = true;
        private String apiKey = "your_lastfm_api_key";
        private String baseUrl = "https://ws.audioscrobbler.com/2.0";
        private int timeout = 5000;
        private int retryAttempts = 3;
    }
    
    @Data
    public static class DeezerConfig {
        private boolean enabled = true;
        private String baseUrl = "https://api.deezer.com";
        private int timeout = 5000;
        private int retryAttempts = 3;
    }
    
    @Data
    public static class LyricsConfig {
        private boolean enabled = true;
        private String lyricsOvhUrl = "https://api.lyrics.ovh/v1";
        private String geniusApiKey = "your_genius_api_key";
        private String geniusBaseUrl = "https://api.genius.com";
        private int timeout = 5000;
        private int retryAttempts = 3;
    }
}