package com.sourav.melodymind.service.impl;

import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.UserSessionDto;
import com.sourav.melodymind.service.UserFavoriteService;
import com.sourav.melodymind.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionServiceImpl implements UserSessionService {
    
    private final UserFavoriteService userFavoriteService;
    
    // In-memory session storage (in production, use Redis or database)
    private final Map<Long, UserSessionDto> userSessions = new ConcurrentHashMap<>();
    
    @Override
    public UserSessionDto createUserSession() {
        Long userId = generateRandomUserId();
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        UserSessionDto session = UserSessionDto.builder()
                .userId(userId)
                .sessionId(sessionId)
                .createdAt(now)
                .lastAccessed(now)
                .expiresAt(now.plusHours(24)) // Session expires in 24 hours
                .isActive(true)
                .favoriteCount(0)
                .build();
        
        userSessions.put(userId, session);
        
        log.info("Created new user session for userId: {} with sessionId: {}", userId, sessionId);
        return session;
    }
    
    @Override
    public boolean isValidUserSession(Long userId) {
        if (userId == null) {
            return false;
        }
        
        UserSessionDto session = userSessions.get(userId);
        if (session == null) {
            log.debug("No session found for userId: {}", userId);
            return false;
        }
        
        if (!session.isActive()) {
            log.debug("Session is inactive for userId: {}", userId);
            return false;
        }
        
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.debug("Session expired for userId: {}", userId);
            // Remove expired session
            userSessions.remove(userId);
            return false;
        }
        
        return true;
    }
    
    @Override
    public UserSessionDto getUserSession(Long userId) {
        if (!isValidUserSession(userId)) {
            return null;
        }
        
        UserSessionDto session = userSessions.get(userId);
        if (session != null) {
            // Update favorite count
            session.setFavoriteCount(userFavoriteService.getFavoriteCount(userId));
        }
        
        return session;
    }
    
    @Override
    public UserSessionDto refreshUserSession(Long userId) {
        UserSessionDto session = userSessions.get(userId);
        if (session == null) {
            log.warn("Attempting to refresh non-existent session for userId: {}", userId);
            return null;
        }
        
        LocalDateTime now = LocalDateTime.now();
        session.setLastAccessed(now);
        session.setExpiresAt(now.plusHours(24)); // Extend expiration
        session.setFavoriteCount(userFavoriteService.getFavoriteCount(userId));
        
        userSessions.put(userId, session);
        
        log.debug("Refreshed session for userId: {}", userId);
        return session;
    }
    
    /**
     * Generates a random user ID that doesn't conflict with existing sessions
     */
    private Long generateRandomUserId() {
        Long userId;
        int attempts = 0;
        int maxAttempts = 100;
        
        do {
            // Generate random ID between 1000000 and 9999999 (7 digits)
            userId = ThreadLocalRandom.current().nextLong(1000000L, 10000000L);
            attempts++;
            
            if (attempts > maxAttempts) {
                log.warn("Max attempts reached generating unique user ID, using timestamp-based ID");
                userId = System.currentTimeMillis() % 10000000L + 1000000L;
                break;
            }
        } while (userSessions.containsKey(userId));
        
        return userId;
    }
    
    /**
     * Cleanup expired sessions (called by scheduler)
     */
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        int removedCount = 0;
        
        userSessions.entrySet().removeIf(entry -> {
            UserSessionDto session = entry.getValue();
            if (session.getExpiresAt().isBefore(now)) {
                log.debug("Removing expired session for userId: {}", entry.getKey());
                return true;
            }
            return false;
        });
        
        if (removedCount > 0) {
            log.info("Cleaned up {} expired user sessions", removedCount);
        }
    }
    
    /**
     * Get session statistics
     */
    public Map<String, Object> getSessionStats() {
        LocalDateTime now = LocalDateTime.now();
        long activeCount = userSessions.values().stream()
                .mapToLong(session -> session.isActive() && session.getExpiresAt().isAfter(now) ? 1 : 0)
                .sum();
        
        return Map.of(
                "totalSessions", userSessions.size(),
                "activeSessions", activeCount,
                "expiredSessions", userSessions.size() - activeCount
        );
    }
}