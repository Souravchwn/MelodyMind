package com.sourav.melodymind.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSessionDto {
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("session_id")
    private String sessionId;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("last_accessed")
    private LocalDateTime lastAccessed;
    
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;
    
    @JsonProperty("is_active")
    private boolean isActive;
    
    @JsonProperty("favorite_count")
    private long favoriteCount;
}