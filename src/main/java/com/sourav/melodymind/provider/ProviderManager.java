package com.sourav.melodymind.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager for music provider plugins.
 * Handles provider registration, prioritization, and lifecycle.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderManager {
    
    private final List<MusicProviderPlugin> providers;
    
    /**
     * Get all enabled providers sorted by priority
     * @return list of enabled providers
     */
    public List<MusicProviderPlugin> getEnabledProviders() {
        return providers.stream()
                .filter(MusicProviderPlugin::isEnabled)
                .sorted(Comparator.comparingInt(MusicProviderPlugin::getPriority))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all providers (enabled and disabled)
     * @return list of all providers
     */
    public List<MusicProviderPlugin> getAllProviders() {
        return providers.stream()
                .sorted(Comparator.comparingInt(MusicProviderPlugin::getPriority))
                .collect(Collectors.toList());
    }
    
    /**
     * Get provider by name
     * @param name provider name
     * @return provider if found, null otherwise
     */
    public MusicProviderPlugin getProviderByName(String name) {
        return providers.stream()
                .filter(provider -> provider.getProviderName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get provider statistics
     * @return provider stats
     */
    public ProviderStats getProviderStats() {
        long totalProviders = providers.size();
        long enabledProviders = providers.stream()
                .mapToLong(provider -> provider.isEnabled() ? 1 : 0)
                .sum();
        
        return ProviderStats.builder()
                .totalProviders(totalProviders)
                .enabledProviders(enabledProviders)
                .disabledProviders(totalProviders - enabledProviders)
                .providerNames(providers.stream()
                        .map(MusicProviderPlugin::getProviderName)
                        .collect(Collectors.toList()))
                .build();
    }
    
    @lombok.Data
    @lombok.Builder
    public static class ProviderStats {
        private long totalProviders;
        private long enabledProviders;
        private long disabledProviders;
        private List<String> providerNames;
    }
}