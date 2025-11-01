package com.sourav.melodymind.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static ResourceNotFoundException songNotFound(Long songId) {
        return new ResourceNotFoundException("Song with ID " + songId + " not found");
    }
    
    public static ResourceNotFoundException userNotFound(Long userId) {
        return new ResourceNotFoundException("User with ID " + userId + " not found");
    }
}