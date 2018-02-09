package com.kronen.buddy.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kronen.buddy.backend.persistence.repositories.PasswordResetTokenRepository;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetTokenService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${token.expiration.minutes}")
    private int tokenExpirationInMinutes;


}
