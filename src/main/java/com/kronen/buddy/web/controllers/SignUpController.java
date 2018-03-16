package com.kronen.buddy.web.controllers;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.service.PlanService;
import com.kronen.buddy.backend.service.UserService;
import com.kronen.buddy.common.enums.PlansEnum;
import com.kronen.buddy.common.enums.RolesEnum;
import com.kronen.buddy.common.utils.UserUtils;
import com.kronen.buddy.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

@Controller
public class SignUpController {

    private static final Logger LOG = LoggerFactory.getLogger(SignUpController.class);

    public static final String SIGNUP_PATH = "/signUp";
    public static final String PAYLOAD_MODEL = "payload";
    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";
    public static final String REDIRECT_HOMEPAGE = "redirect:/";
    public static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";
    public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";
    public static final String SIGNED_UP_MESSAGE_KEY = "signedUp";
    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    @Autowired
    public UserService userService;

    @Autowired
    public PlanService planService;

    @GetMapping(value = SIGNUP_PATH)
    public String signUpGet(@RequestParam("planId") Optional<Integer> planId, ModelMap model) {

        if(!planId.isPresent()) {
            return REDIRECT_HOMEPAGE;
        } else if(!planId.get().equals(PlansEnum.BASIC.getId())
                && !planId.get().equals(PlansEnum.PRO.getId())) {
            throw new IllegalArgumentException("Plan id is not valid");
        }
        model.addAttribute(PAYLOAD_MODEL, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }

    @PostMapping(value = SIGNUP_PATH)
    public String signUpPost(@RequestParam(name = "planId", required = false) int planId,
                             @ModelAttribute(PAYLOAD_MODEL) @Valid ProAccountPayload payload,
                             ModelMap model) throws SQLException {
        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()) {
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id does not exist");
            return SUBSCRIPTION_VIEW_NAME;
        }

        checkForDuplicates(payload, model);

        boolean duplicates = false;

        List<String> errorMessages = new ArrayList<>();

        if(model.containsKey(DUPLICATED_USERNAME_KEY)) {
            LOG.warn("The username already exists. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            errorMessages.add("Username already exists");
            duplicates = true;
        }

        if(model.containsKey(DUPLICATED_EMAIL_KEY)) {
            LOG.warn("The email already exists. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            errorMessages.add("Email already exists");
            duplicates = true;
        }

        if(duplicates) {
            model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
            return SUBSCRIPTION_VIEW_NAME;
        }

        // There are certain info that the user doesn't set such as profile image URL,
        // plans and roles
        LOG.debug("Transforming user payload into User domain object");
        User user = UserUtils.fromWebUserToDomainUser(payload);

        // Sets the Plan and the Roles (depending on the choice plan)
        Optional<Plan> selectedPlan = planService.findByPlanId(planId);
        if(selectedPlan.isPresent()) {
            LOG.error("The plan id {} could not be found. Throwing exception");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id not found");
        }
        user.setPlan(selectedPlan.get());

        User registeredUser;

        Set<UserRole> roles = new HashSet<>();
        if(planId == PlansEnum.PRO.getId()) {
            roles.add(new UserRole(user, new Role(RolesEnum.PRO)));
            registeredUser = Optional.ofNullable(userService.createUser(user, PlansEnum.PRO, roles))
                    .orElseThrow(SQLException::new);
        } else {
            roles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
            registeredUser = Optional.ofNullable(userService.createUser(user, PlansEnum.BASIC, roles))
                    .orElseThrow(SQLException::new);
        }

        // Auto login the registered user
        Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser,
                null, registeredUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        LOG.info("User {} created successfully", registeredUser.getUsername());

        model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");

        return SUBSCRIPTION_VIEW_NAME;
    }

    /**
     * Checks if the username/email are duplicates and sets error flags in the model.
     * Side effect: the method might set attributes on Model
     * @param payload The form model for the account payload
     * @param model The model map
     */
    private void checkForDuplicates(@Valid ProAccountPayload payload, ModelMap model) {
        if(userService.findByUserName(payload.getUsername()).isPresent()) {
            model.addAttribute(DUPLICATED_USERNAME_KEY, true);
        }

        if(userService.findByEmail(payload.getEmail()).isPresent()) {
            model.addAttribute(DUPLICATED_EMAIL_KEY, true);
        }
    }
}
