package com.kronen.buddy.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ForgotMyPasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    private static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";
    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    public String forgotPassword() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

}
