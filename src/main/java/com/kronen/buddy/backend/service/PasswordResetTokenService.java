package com.kronen.buddy.backend.service;

import com.kronen.buddy.backend.persistence.domain.backend.PasswordResetToken;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.repositories.PasswordResetTokenRepository;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


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

    /**
     * Retrieves a Password Reset Token for the given token id.
     *
     * @param token The token to be returned
     * @return A (Optional) Password Reset Token
     */
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    /**
     * Creates a new Password Reset Token for the user identified by the given email
     *
     * @param email The email uniquely identifying the user
     * @return a (Optional) new Password Reset Token for the user identified by the given email
     */
    @Transactional
    public Optional<PasswordResetToken> createPasswordResetToken(String email) {
        Optional<PasswordResetToken> passwordResetToken = Optional.empty();

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            passwordResetToken = Optional.of(new PasswordResetToken(token, user.get(), now, tokenExpirationInMinutes));
            passwordResetToken = Optional.of(passwordResetTokenRepository.save(passwordResetToken.get()));
            LOG.debug("Successfully created token {} for user {}", token, user.get().getUsername());
        } else {
            LOG.warn("We couldn't find a user for the given email {}", email);
        }

        return passwordResetToken;
    }

}
