package com.sourav.melodymind.service;

import com.sourav.melodymind.dto.UserSessionDto;

public interface UserSessionService {
    
    /**
     * Creates a new user session with a random user ID
     * @return UserSessionDto with generated user ID and session info
     */
    UserSessionDto createUserSession();
    
    /**
     * Validates if a user session exists and is valid
     * @param userId the user ID to validate
     * @return true if session is valid
     */
    boolean isValidUserSession(Long userId);
    
    /**
     * Gets user session information
     * @param userId the user ID
     * @return UserSessionDto with session info or null if not found
     */
    UserSessionDto getUserSession(Long userId);
    
    /**
     * Refreshes user session timestamp
     * @param userId the user ID
     * @return updated UserSessionDto
     */
    UserSessionDto refreshUserSession(Long userId);
}