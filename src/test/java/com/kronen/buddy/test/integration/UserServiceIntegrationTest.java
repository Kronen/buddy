package com.kronen.buddy.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.service.UserService;
import com.kronen.buddy.common.enums.PlansEnum;
import com.kronen.buddy.common.enums.RolesEnum;
import com.kronen.buddy.common.utils.UserUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest {
    
    @Autowired
    private UserService userService;
    
    @Rule public TestName testName = new TestName();
        
    @Test
    public void createNewUser() {
	String username = testName.getMethodName();
	String email = testName.getMethodName() + "@buddy.com";
	
	User basicUser = UserUtils.createBasicUser(username, email);	
	Set<UserRole> userRoles = new HashSet<>();
	userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
	
	User user = userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
	assertThat(user).isNotNull();
	assertThat(user.getId()).isNotNull();
    }
}
