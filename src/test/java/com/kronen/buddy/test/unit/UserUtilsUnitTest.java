package com.kronen.buddy.test.unit;

import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.common.utils.UserUtils;
import com.kronen.buddy.web.controllers.ForgotMyPasswordController;
import com.kronen.buddy.web.domain.frontend.BasicAccountPayload;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    private PodamFactory podamFactory;

    @Before
    public void init() {
        mockHttpServletRequest = new MockHttpServletRequest();
        podamFactory = new PodamFactoryImpl();
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

    @Test
    public void mapWebUserToDomainUser() {
        BasicAccountPayload webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayload.class);
        webUser.setEmail("me@example.com");

        User user = UserUtils.fromWebUserToDomainUser(webUser);
        assertThat(user).isNotNull();

        assertThat(webUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(webUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(webUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(webUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(webUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(webUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(webUser.getCountry()).isEqualTo(user.getCountry());
        assertThat(webUser.getDescription()).isEqualTo(user.getDescription());
    }
}
