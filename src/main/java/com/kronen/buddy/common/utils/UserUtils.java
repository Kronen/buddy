package com.kronen.buddy.common.utils;

import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.web.controllers.ForgotMyPasswordController;
import com.kronen.buddy.web.domain.frontend.BasicAccountPayload;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {

    private UserUtils() {
        throw new AssertionError("Not instantiable");
    }

    /**
     * Creates a user with basic attributes set
     *
     * @param username The username
     * @param email    The email
     * @return A User entity
     */
    public static User createBasicUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setEmail(email);
        user.setPhoneNumber("666777888");
        user.setCountry("ES");
        user.setEnabled(true);
        user.setDescription("A basic user");
        user.setProfileImageUrl("https://www.images.com/basic_user.png");
        return user;
    }

    public static String createPasswordResetUrl(HttpServletRequest request, long userId, String token) {
        UriComponents uc =
                ServletUriComponentsBuilder
                        .fromRequest(request)
                        .replacePath(ForgotMyPasswordController.CHANGE_PASSWORD_PATH)
                        .queryParam("id", userId)
                        .queryParam("token", token)
                        .build()
                        .encode();

        return uc.toString();
    }

    public static <T extends BasicAccountPayload> User fromWebUserToDomainUser(T frontendPayload) {
        User user = new User();
        user.setUsername(frontendPayload.getUsername());
        user.setPassword(frontendPayload.getPassword());
        user.setFirstName(frontendPayload.getFirstName());
        user.setLastName(frontendPayload.getLastName());
        user.setEmail(frontendPayload.getEmail());
        user.setPhoneNumber(frontendPayload.getPhoneNumber());
        user.setCountry(frontendPayload.getCountry());
        user.setDescription(frontendPayload.getDescription());
        user.setEnabled(true);

        return user;
    }
}
