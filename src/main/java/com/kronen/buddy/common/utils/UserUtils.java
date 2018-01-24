package com.kronen.buddy.common.utils;

import com.kronen.buddy.backend.persistence.domain.backend.User;

public class UserUtils {
    
    private UserUtils() {
	throw new AssertionError("Not instantiable");
    }
    
    public static User createBasicUser() {
	User user = new User();
	user.setUsername("basicUser");
	user.setPassword("secrets");
	user.setFirstName("firstname");
	user.setLastName("lastname");
	user.setEmail("kronen@mail.com");
	user.setPhoneNumber("666777888");
	user.setCountry("ES");
	user.setEnabled(true);
	user.setDescription("A basic user");
	user.setProfileImageUrl("https://www.images.com/basic_user.png");
	return user;
    }
}
