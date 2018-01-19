package com.kronen.buddy.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kronen.buddy.backend.service.EmailService;

@Controller
public class ContactController {

    private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);
    
    public static final String MODEL_FEEDBACK = "feedback";
    
    public static final String CONTACT_US_VIEW_NAME = "contact/contact";
    
    @Autowired
    private EmailService emailService;
}
