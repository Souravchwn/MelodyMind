package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.ApiResponse;
import com.sourav.melodymind.dto.FavoriteRequestDto;
import com.sourav.melodymind.dto.SongDto;
import com.sourav.melodymind.service.UserFavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.FAVORITES_FULL_PATH)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FavoriteController {
    
    private final UserFavoriteService userFavoriteService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<SongDto>>> getUserFavorites(
            @RequestParam(ApiConstants.PARAM_USER_ID) Long userId) {
        
        log.info("Received request to get favorites for user: {}", userId);
        
        List<SongDto> favorites = userFavoriteService.getUserFavorites(userId);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_FAVORITES_FETCHED, 
                favorites
        ));
    }
    
    @PostMapping(ApiConstants.FAVORITES_ADD)
    public ResponseEntity<ApiResponse<Boolean>> addToFavorites(
            @RequestBody FavoriteRequestDto request) {
        
        log.info("Received request to add favorite: {}", request);
        
        // Validate request manually to provide better error messages
        if (request.getUserId() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ApplicationConstants.VALIDATION_USER_ID_REQUIRED));
        }
        
        if (request.getSongId() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ApplicationConstants.VALIDATION_SONG_ID_REQUIRED));
        }
        
        boolean added = userFavoriteService.addToFavorites(request);
        
        if (added) {
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_FAVORITE_ADDED, 
                    true
            ));
        } else {
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_FAVORITE_ALREADY_EXISTS, 
                    false
            ));
        }
    }
    
    @PostMapping(ApiConstants.FAVORITES_REMOVE)
    public ResponseEntity<ApiResponse<Boolean>> removeFromFavorites(
            @RequestBody FavoriteRequestDto request) {
        
        log.info("Received request to remove favorite: {}", request);
        
        // Validate request manually to provide better error messages
        if (request.getUserId() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ApplicationConstants.VALIDATION_USER_ID_REQUIRED));
        }
        
        if (request.getSongId() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ApplicationConstants.VALIDATION_SONG_ID_REQUIRED));
        }
        
        boolean removed = userFavoriteService.removeFromFavorites(request);
        
        if (removed) {
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_FAVORITE_REMOVED, 
                    true
            ));
        } else {
            return ResponseEntity.ok(ApiResponse.success(
                    ApplicationConstants.MSG_FAVORITE_NOT_EXISTS, 
                    false
            ));
        }
    }
    
    @GetMapping(ApiConstants.FAVORITES_CHECK)
    public ResponseEntity<ApiResponse<Boolean>> isFavorite(
            @RequestParam(ApiConstants.PARAM_USER_ID) Long userId,
            @RequestParam(ApiConstants.PARAM_SONG_ID) Long songId) {
        
        log.info("Checking if song {} is favorite for user {}", songId, userId);
        
        boolean isFavorite = userFavoriteService.isFavorite(userId, songId);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_SUCCESS, 
                isFavorite
        ));
    }
    
    @GetMapping(ApiConstants.FAVORITES_COUNT)
    public ResponseEntity<ApiResponse<Long>> getFavoriteCount(
            @RequestParam(ApiConstants.PARAM_USER_ID) Long userId) {
        
        log.info("Getting favorite count for user: {}", userId);
        
        long count = userFavoriteService.getFavoriteCount(userId);
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_FAVORITE_COUNT_FETCHED, 
                count
        ));
    }
}