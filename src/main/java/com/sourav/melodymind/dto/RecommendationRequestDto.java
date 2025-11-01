package com.sourav.melodymind.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestDto {
    private String genre;
    private String language;
    private String region;
    private String mood;
    private Integer limit = 20;
    private Long userId;
}