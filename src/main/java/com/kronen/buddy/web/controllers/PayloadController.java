package com.kronen.buddy.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PayloadController {
    
    private static final String PAYLOAD_VIEW_NAME = "payload";
    
    @RequestMapping("/payload")
    public String payload() {
	return PAYLOAD_VIEW_NAME;
    }
}
