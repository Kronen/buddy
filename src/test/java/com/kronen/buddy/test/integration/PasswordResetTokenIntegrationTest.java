package com.kronen.buddy.test.integration;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kronen.buddy.backend.persistence.domain.backend.PasswordResetToken;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.repositories.PasswordResetTokenRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest {
    
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Rule public TestName testName = new TestName();
    
    @Value("${token.expiration.minutes}")
    private int expirationTimeInMinutes;

    @Before
    public void init() {
	assertThat(expirationTimeInMinutes != 0).isTrue();
    }
    
    @Test
    public void testTokenExpirationDate() {
	User user = createUser(testName);
	LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
	String token = UUID.randomUUID().toString();
	LocalDateTime expectedTime = now.plusMinutes(expirationTimeInMinutes);
	
	PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
	LocalDateTime actualTime = passwordResetToken.getExpiryDate();
	
	assertThat(actualTime).isEqualTo(expectedTime);
    }
    
    @Test
    public void testFindTokenByTokenValue() {
	User user = createUser(testName);
	LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
	String token = UUID.randomUUID().toString();
	
	createPasswordResetToken(token, user, now);
	Optional<PasswordResetToken> retrievedPasswordResetToken = passwordResetTokenRepository.findByToken(token);
	
	assertThat(retrievedPasswordResetToken.isPresent()).isTrue();
	assertThat(retrievedPasswordResetToken.get().getId()).isNotNull();
	assertThat(retrievedPasswordResetToken.get().getUser()).isNotNull();
    }
    
    @Test
    public void testDeleteToken() {
	User user = createUser(testName);
	LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
	String token = UUID.randomUUID().toString();
	
	PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
	long tokenId = passwordResetToken.getId();
	passwordResetTokenRepository.deleteById(tokenId);	
	Optional<PasswordResetToken> shouldNotExistToken = passwordResetTokenRepository.findById(tokenId);
	
	assertThat(shouldNotExistToken.isPresent()).isFalse();	
    }
    
    @Test
    public void testCascadeDeleteFromUserEntity() {
	User user = createUser(testName);
	LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
	String token = UUID.randomUUID().toString();
	
	createPasswordResetToken(token, user, now);	
	userRepository.deleteById(user.getId());
	Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findByUserId(user.getId());
	
	assertThat(shouldBeEmpty).isEmpty();	
    }
    
    @Test
    public void testMultipleTokensAreReturnedWhenQueryingByUserId() {
	User user = createUser(testName);
	LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
	String token1 = UUID.randomUUID().toString();
	String token2 = UUID.randomUUID().toString();
	String token3 = UUID.randomUUID().toString();
	
	Set<PasswordResetToken> actualTokens = new HashSet<>();
	actualTokens.add(createPasswordResetToken(token1, user, now));
	actualTokens.add(createPasswordResetToken(token2, user, now));
	actualTokens.add(createPasswordResetToken(token3, user, now));
	
	passwordResetTokenRepository.saveAll(actualTokens);	
	Optional<User> foundUser = userRepository.findById(user.getId());
	
	Set<PasswordResetToken> expectedTokens = passwordResetTokenRepository.findByUserId(foundUser.get().getId());
	 
	assertThat(expectedTokens).hasSize(actualTokens.size());
	List<String> actualTokensList = actualTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
	List<String> expectedTokensList = expectedTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
	assertThat(actualTokensList).containsExactlyElementsOf(expectedTokensList);	
    }

    private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now) {
	PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, expirationTimeInMinutes);
	passwordResetTokenRepository.save(passwordResetToken);
	return passwordResetToken;
    }
}
