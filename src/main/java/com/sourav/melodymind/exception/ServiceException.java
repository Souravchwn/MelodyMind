package com.sourav.melodymind.exception;

public class ServiceException extends RuntimeException {
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static ServiceException providerError(String providerName, String error) {
        return new ServiceException("Error from provider " + providerName + ": " + error);
    }
    
    public static ServiceException cacheError(String operation) {
        return new ServiceException("Cache error during " + operation);
    }
}