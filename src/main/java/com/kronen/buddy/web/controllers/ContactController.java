package com.kronen.buddy.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kronen.buddy.backend.service.EmailService;
import com.kronen.buddy.web.domain.frontend.FeedbackBean;

@Controller
public class ContactController {

    private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);
    
    public static final String MODEL_FEEDBACK = "feedback";
    
    public static final String CONTACT_US_VIEW_NAME = "contact/contact";
    
    @Autowired
    private EmailService emailService;
    
    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactGet(ModelMap model) {	
	model.addAttribute(MODEL_FEEDBACK, new FeedbackBean());
	return CONTACT_US_VIEW_NAME;
    }
    
    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contactPost(@ModelAttribute(MODEL_FEEDBACK) FeedbackBean feedback, ModelMap model) {	
	LOG.debug("Feedback bean content {}", feedback);
	emailService.sendFeedbackEmail(feedback);
	return CONTACT_US_VIEW_NAME;
    }
}
