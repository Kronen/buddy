package com.kronen.buddy.test.integration;

import com.kronen.buddy.backend.persistence.domain.backend.PasswordResetToken;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.service.PasswordResetTokenService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordResetTokenServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Rule public TestName testName = new TestName();

    @Test
    public void testCreateNewTokenForUserEmail() {
        User user = createUser(testName);

        Optional<PasswordResetToken> passwordResetToken;
        passwordResetToken = passwordResetTokenService.createPasswordResetToken(user.getEmail());
        assertThat(passwordResetToken.isPresent()).isTrue();
        assertThat(passwordResetToken.get().getToken()).isNotNull();
    }

    @Test
    public void testFindByToken() throws Exception {
        User user = createUser(testName);

        Optional<PasswordResetToken> passwordResetToken;
        passwordResetToken = passwordResetTokenService.createPasswordResetToken(user.getEmail());

        Optional<PasswordResetToken> passwordResetTokenRetrieved = passwordResetTokenService
                .findByToken(passwordResetToken.get().getToken());
        assertThat(passwordResetToken).isEqualTo(passwordResetTokenRetrieved);
    }
}
