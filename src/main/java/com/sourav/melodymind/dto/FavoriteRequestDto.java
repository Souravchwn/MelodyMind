package com.sourav.melodymind.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDto {
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("songId")
    private Long songId;
}