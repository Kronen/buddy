package com.kronen.buddy.backend.service;

import org.springframework.mail.SimpleMailMessage;

import com.kronen.buddy.web.domain.frontend.FeedbackBean;

/**
 * Contract for EmailService
 *
 * @author Kronen
 */
public interface EmailService {

    /**
     * Sends an email with the content in the Feedback Bean
     *
     * @param feedbackBean The feedback bean
     */
    public void sendFeedbackEmail(FeedbackBean feedbackBean);

    /**
     * Sends an email with the content of the Simple Mail Message object
     *
     * @param message The object containing the email content
     */
    public void sendGenericEmailMessage(SimpleMailMessage message);


}
