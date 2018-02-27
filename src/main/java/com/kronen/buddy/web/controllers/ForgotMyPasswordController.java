package com.kronen.buddy.web.controllers;

import com.kronen.buddy.backend.persistence.domain.backend.PasswordResetToken;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.service.EmailService;
import com.kronen.buddy.backend.service.I18NService;
import com.kronen.buddy.backend.service.PasswordResetTokenService;
import com.kronen.buddy.backend.service.UserService;
import com.kronen.buddy.common.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Controller
public class ForgotMyPasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    public static final String FORGOT_PASSWORD_PATH = "/forgotmypassword";
    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";
    private static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";
    private static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changePassword";

    private static final String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";
    private static final String MESSAGE_ATTRIBUTE_NAME = "message";
    private static final String MAIL_SENT_KEY_ATTRIBUTE_NAME = "mailSent";


    @Value("${webmaster.email}")
    private String webmasterEmail;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @GetMapping(value = FORGOT_PASSWORD_PATH)
    public String forgotPasswordGet() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @PostMapping(value = FORGOT_PASSWORD_PATH)
    public String forgotPasswordPost(HttpServletRequest request, @RequestParam("email") String email,
                                 ModelMap model) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenService
                .createPasswordResetToken(email);
        if(!passwordResetToken.isPresent()) {
            LOG.warn("Couldn't find a password reset token for email {}", email);
        } else {
            User user = passwordResetToken.get().getUser();
            String token = passwordResetToken.get().getToken();

            String passwordResetUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
            LOG.debug("PasswordResetUrl: {}", passwordResetUrl);

            String emailText = i18NService.getMessage("forgotmypassword.email.text", request.getLocale());
            String emailSubject = i18NService.getMessage("forgotmypassword.email.subject", request.getLocale());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(emailSubject);
            mailMessage.setFrom(webmasterEmail);
            mailMessage.setText(emailText + "\r\n" + passwordResetUrl);

            emailService.sendGenericEmailMessage(mailMessage);
        }

        model.addAttribute(MAIL_SENT_KEY_ATTRIBUTE_NAME, "true");
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @GetMapping(value = CHANGE_PASSWORD_PATH)
    public String changeUserPasswordGet(@RequestParam("id") Long id, @RequestParam("token") String token,
                                     Locale locale, ModelMap model) {
        if(id == null || StringUtils.isEmpty(token)) {
            LOG.error("Invalid user id {} or token value {}", id, token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("resetPassword.tokenOrId.invalid", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenService.findByToken(token);

        if(!passwordResetToken.isPresent()) {
            LOG.error("A token couldn't be found with value {}", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("resetPassword.token.notFound", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordResetToken.get().getUser();
        if(!user.getId().equals(id)) {
            LOG.error("The user id {} passed as parameter does not match user id {} associated with the token {}",
                    id, user.getId(), token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("resetPassword.token.invalid", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if(LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.get().getExpiryDate())) {
            LOG.error("The token {} has expired", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("resetPassword.token.expired", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        model.addAttribute("principalId", user.getId());

        Authentication auth = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @PostMapping(value = CHANGE_PASSWORD_PATH)
    public String changeUserPasswordPost(@RequestParam("principal_id") Long userId,
                                     @RequestParam("password") String password,
                                     Locale locale, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            LOG.error("An unauthenticated user tried to invoke the reset password POST method");
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("resetPassword.token.unauthorized", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User) auth.getPrincipal();
        if(!user.getId().equals(userId)) {
            LOG.error("Security breach! User {} is trying to make a password reset on behalf of user {}", user.getId(),
                    userId);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("resetPassword.token.unauthorized", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updateUserPassword(userId, password);
        LOG.info("Password successfully updated for user {}", user.getUsername());

        model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");

        return CHANGE_PASSWORD_VIEW_NAME;
    }
}
