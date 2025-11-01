package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.API_BASE)
public class HealthController {

    @GetMapping(ApiConstants.HEALTH)
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> healthData = Map.of(
            "status", ApplicationConstants.STATUS_UP,
            "timestamp", LocalDateTime.now(),
            "service", ApplicationConstants.APPLICATION_DESCRIPTION,
            "version", ApplicationConstants.APPLICATION_VERSION
        );
        
        return ResponseEntity.ok(ApiResponse.success(
                ApplicationConstants.MSG_SERVICE_HEALTHY, 
                healthData
        ));
    }
}