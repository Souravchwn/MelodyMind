package com.sourav.melodymind.service;

import com.sourav.melodymind.dto.FavoriteRequestDto;
import com.sourav.melodymind.dto.SongDto;

import java.util.List;

public interface UserFavoriteService {
    
    List<SongDto> getUserFavorites(Long userId);
    
    boolean addToFavorites(FavoriteRequestDto request);
    
    boolean removeFromFavorites(FavoriteRequestDto request);
    
    boolean isFavorite(Long userId, Long songId);
    
    long getFavoriteCount(Long userId);
}