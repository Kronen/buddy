package com.kronen.buddy.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.kronen.buddy.web.domain.frontend.FeedbackBean;

public abstract class AbstractEmailService implements EmailService {
    
    @Value("${default.to.address}")
    private String defaultToAddress;
    
    /**
     * Creates a Simple Mail Message from a Feedback Bean
     * @param feedbackBean The Feedback bean
     * @return
     */
    protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackBean(FeedbackBean feedback) {
	SimpleMailMessage message = new SimpleMailMessage();
	message.setTo(defaultToAddress);
	message.setFrom(feedback.getEmail());
	message.setSubject("[Buddy] Feedback received from " + feedback.getFirstName() + " " + feedback.getLastName() + "!");
	message.setText(feedback.getFeedback());
	return message;
    }

    @Override
    public void sendFeedbackEmail(FeedbackBean feedbackBean) {
	sendGenericEmailMessage(prepareSimpleMailMessageFromFeedbackBean(feedbackBean));
    }

}
