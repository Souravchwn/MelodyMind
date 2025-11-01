package com.sourav.melodymind.controller;

import com.sourav.melodymind.constants.ApiConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping(ApiConstants.WEB_ROOT)
    public String index() {
        return "forward:/index.html";
    }
    
    @GetMapping(ApiConstants.WEB_DASHBOARD)
    public String dashboard() {
        return "forward:/index.html";
    }
    
    @GetMapping(ApiConstants.WEB_ADMIN)
    public String admin() {
        return "forward:/admin.html";
    }
    
    @GetMapping(ApiConstants.WEB_TEST)
    public String test() {
        return "forward:/test.html";
    }
    
    @GetMapping(ApiConstants.WEB_ABOUT)
    public String about() {
        return "forward:/about.html";
    }
}