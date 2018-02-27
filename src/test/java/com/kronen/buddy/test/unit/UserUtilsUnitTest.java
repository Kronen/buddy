package com.kronen.buddy.test.unit;

import com.kronen.buddy.common.utils.UserUtils;
import com.kronen.buddy.web.controllers.ForgotMyPasswordController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    @Before
    public void init() {
        mockHttpServletRequest = new MockHttpServletRequest();
    }

    @Test
    public void testPasswordResetEmail() {
        mockHttpServletRequest.setServerPort(8080);

        String token = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
        long userId = 123456;

        String expectedUrl = "http://localhost:8080" +
                ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userId +
                "&token=" + token;

        String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);

        assertThat(expectedUrl).isEqualTo(actualUrl);
    }
}
