package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.ApiResponse;
import com.sourav.melodymind.provider.MusicProviderPlugin;
import com.sourav.melodymind.provider.ProviderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstants.PROVIDERS_FULL_PATH)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProviderController {
    
    private final ProviderManager providerManager;
    
    @GetMapping(ApiConstants.PROVIDERS_STATUS)
    public ResponseEntity<ApiResponse<ProviderManager.ProviderStats>> getProviderStatus() {
        log.info("Fetching provider status");
        
        ProviderManager.ProviderStats stats = providerManager.getProviderStats();
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_PROVIDER_STATUS_FETCHED, 
                stats
        ));
    }
    
    @GetMapping(ApiConstants.PROVIDERS_LIST)
    public ResponseEntity<ApiResponse<List<ProviderInfo>>> getProviderList() {
        log.info("Fetching provider list");
        
        List<ProviderInfo> providerInfos = providerManager.getAllProviders()
                .stream()
                .map(provider -> ProviderInfo.builder()
                        .name(provider.getProviderName())
                        .enabled(provider.isEnabled())
                        .priority(provider.getPriority())
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_PROVIDER_LIST_FETCHED, 
                providerInfos
        ));
    }
    
    @GetMapping(ApiConstants.PROVIDERS_ENABLED)
    public ResponseEntity<ApiResponse<List<String>>> getEnabledProviders() {
        log.info("Fetching enabled providers");
        
        List<String> enabledProviders = providerManager.getEnabledProviders()
                .stream()
                .map(MusicProviderPlugin::getProviderName)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_ENABLED_PROVIDERS_FETCHED, 
                enabledProviders
        ));
    }
    
    @lombok.Data
    @lombok.Builder
    public static class ProviderInfo {
        private String name;
        private boolean enabled;
        private int priority;
    }
}