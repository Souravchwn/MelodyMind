package com.sourav.melodymind.service.impl;

import com.sourav.melodymind.dto.FavoriteRequestDto;
import com.sourav.melodymind.dto.SongDto;
import com.sourav.melodymind.entity.Song;
import com.sourav.melodymind.entity.UserFavorite;
import com.sourav.melodymind.exception.ResourceNotFoundException;
import com.sourav.melodymind.repository.SongRepository;
import com.sourav.melodymind.repository.UserFavoriteRepository;
import com.sourav.melodymind.service.UserFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFavoriteServiceImpl implements UserFavoriteService {
    
    private final UserFavoriteRepository userFavoriteRepository;
    private final SongRepository songRepository;
    
    @Override
    public List<SongDto> getUserFavorites(Long userId) {
        log.info("Fetching favorites for user: {}", userId);
        
        List<UserFavorite> favorites = userFavoriteRepository.findByUserIdWithSongs(userId);
        
        return favorites.stream()
                .map(favorite -> convertToDto(favorite.getSong(), true))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public boolean addToFavorites(FavoriteRequestDto request) {
        log.info("Adding song {} to favorites for user {}", request.getSongId(), request.getUserId());
        
        // Check if already exists
        if (userFavoriteRepository.existsByUserIdAndSongId(request.getUserId(), request.getSongId())) {
            log.warn("Song {} is already in favorites for user {}", request.getSongId(), request.getUserId());
            return false;
        }
        
        // Verify song exists
        Optional<Song> song = songRepository.findById(request.getSongId());
        if (song.isEmpty()) {
            log.error("Song with id {} not found", request.getSongId());
            throw new ResourceNotFoundException("Song with ID " + request.getSongId() + " not found");
        }
        
        UserFavorite favorite = UserFavorite.builder()
                .userId(request.getUserId())
                .songId(request.getSongId())
                .build();
        
        userFavoriteRepository.save(favorite);
        log.info("Successfully added song {} to favorites for user {}", request.getSongId(), request.getUserId());
        return true;
    }
    
    @Override
    @Transactional
    public boolean removeFromFavorites(FavoriteRequestDto request) {
        log.info("Removing song {} from favorites for user {}", request.getSongId(), request.getUserId());
        
        if (!userFavoriteRepository.existsByUserIdAndSongId(request.getUserId(), request.getSongId())) {
            log.warn("Song {} is not in favorites for user {}", request.getSongId(), request.getUserId());
            return false;
        }
        
        userFavoriteRepository.deleteByUserIdAndSongId(request.getUserId(), request.getSongId());
        log.info("Successfully removed song {} from favorites for user {}", request.getSongId(), request.getUserId());
        return true;
    }
    
    @Override
    public boolean isFavorite(Long userId, Long songId) {
        return userFavoriteRepository.existsByUserIdAndSongId(userId, songId);
    }
    
    @Override
    public long getFavoriteCount(Long userId) {
        return userFavoriteRepository.countByUserId(userId);
    }
    
    private SongDto convertToDto(Song song, boolean isFavorite) {
        return SongDto.builder()
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
                .isFavorite(isFavorite)
                .build();
    }
}