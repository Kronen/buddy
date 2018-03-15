package com.kronen.buddy.web.controllers;

import com.kronen.buddy.common.enums.PlansEnum;
import com.kronen.buddy.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {

    private static final Logger LOG = LoggerFactory.getLogger(SignUpController.class);

    public static final String SIGNUP_PATH = "/signUp";
    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";
    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";

    @GetMapping(value = SIGNUP_PATH)
    public String signUpGet(@RequestParam("planId") int planId, ModelMap model) {
        if(planId != PlansEnum.BASIC.getId()) {
            throw new IllegalArgumentException("Plan is not valid");
        }
        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }

    @PostMapping(value = SIGNUP_PATH)
    public String signUpPost(@RequestParam("planId") int planId, ModelMap model) {
        if(planId != PlansEnum.BASIC.getId()) {
            throw new IllegalArgumentException("Plan is not valid");
        }
        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }
}
