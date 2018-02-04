package com.kronen.buddy.common.utils;

import com.kronen.buddy.backend.persistence.domain.backend.User;

public class UserUtils {
    
    private UserUtils() {
	throw new AssertionError("Not instantiable");
    }
    
    /**
     * Creates a user with basic attributes set
     * 
     * @param username The username
     * @param email The email
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
}
