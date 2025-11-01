package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import com.sourav.melodymind.constants.ApplicationConstants;
import com.sourav.melodymind.dto.ApiResponse;
import com.sourav.melodymind.dto.UserSessionDto;
import com.sourav.melodymind.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ApiConstants.API_BASE + "/session")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserSessionController {
    
    private final UserSessionService userSessionService;
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserSessionDto>> createUserSession() {
        log.info("Creating new user session");
        
        UserSessionDto session = userSessionService.createUserSession();
        
        return ResponseEntity.ok(ApiResponse.success(
                "User session created successfully", 
                session
        ));
    }
    
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserSessionDto>> getUserSessionInfo(
            @RequestParam(ApiConstants.PARAM_USER_ID) Long userId) {
        
        log.info("Getting session info for userId: {}", userId);
        
        if (!userSessionService.isValidUserSession(userId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid or expired user session"));
        }
        
        UserSessionDto session = userSessionService.getUserSession(userId);
        
        return ResponseEntity.ok(ApiResponse.success(
                "User session info retrieved successfully", 
                session
        ));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<UserSessionDto>> refreshUserSession(
            @RequestParam(ApiConstants.PARAM_USER_ID) Long userId) {
        
        log.info("Refreshing session for userId: {}", userId);
        
        if (!userSessionService.isValidUserSession(userId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid or expired user session"));
        }
        
        UserSessionDto session = userSessionService.refreshUserSession(userId);
        
        return ResponseEntity.ok(ApiResponse.success(
                "User session refreshed successfully", 
                session
        ));
    }
    
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateUserSession(
            @RequestParam(ApiConstants.PARAM_USER_ID) Long userId) {
        
        log.debug("Validating session for userId: {}", userId);
        
        boolean isValid = userSessionService.isValidUserSession(userId);
        
        return ResponseEntity.ok(ApiResponse.success(
                isValid ? "User session is valid" : "User session is invalid", 
                isValid
        ));
    }
}