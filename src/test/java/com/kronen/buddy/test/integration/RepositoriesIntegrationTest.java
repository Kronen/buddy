package com.kronen.buddy.test.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.persistence.repositories.PlanRepository;
import com.kronen.buddy.backend.persistence.repositories.RoleRepository;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;


@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoriesIntegrationTest {
    
    private static final long BASIC_PLAN_ID = 1;

    private static final long BASIC_ROLE_ID = 1;

    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Before
    public void init() {
	assertNotNull(planRepository);
	assertNotNull(roleRepository);
	assertNotNull(userRepository);
    }
    
    @Test
    public void createNewPlan() {
	Plan basicPlan = createBasicPlan();
	planRepository.save(basicPlan);
	Optional<Plan> retrievedPlan = planRepository.findById(BASIC_PLAN_ID);
	assertTrue(retrievedPlan.isPresent());
    }
    
    @Test
    public void createNewRole() {
	Role basicRole = createBasicRole();
	roleRepository.save(basicRole);
	Optional<Role> retrievedRole = roleRepository.findById(BASIC_ROLE_ID);
	assertTrue(retrievedRole.isPresent());
    }
    
    @Test
    public void createNewUser() {
	Plan basicPlan = createBasicPlan();
	planRepository.save(basicPlan);
	
	User basicUser = createBasicUser();
	basicUser.setPlan(basicPlan);
	
	Set<UserRole> userRoles = new HashSet<>();
	UserRole userRole = new UserRole();
	userRole.setUser(basicUser);
	userRole.setRole(createBasicRole());
	userRoles.add(userRole);
	
	basicUser.getUserRoles().addAll(userRoles);
	
	for(UserRole ur : userRoles) {
	    roleRepository.save(ur.getRole());
	}
	
	basicUser = userRepository.save(basicUser);
	
	Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
	assertTrue(newlyCreatedUser.isPresent());
	assertTrue(newlyCreatedUser.get().getId() != 0);
	assertNotNull(newlyCreatedUser.get().getPlan());
	Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.get().getUserRoles();
	assertNotNull(newlyCreatedUserUserRoles);
	for(UserRole ur : newlyCreatedUserUserRoles) {
	    assertNotNull(ur.getRole());
	    assertNotNull(ur.getRole().getId());
	}
	
	
    }

    private Role createBasicRole() {
	Role role = new Role();
	role.setId(BASIC_ROLE_ID);
	role.setName("ROLE_USER");
	return role;
    }

    private Plan createBasicPlan() {
	Plan plan = new Plan();
	plan.setId(BASIC_PLAN_ID);
	plan.setName("Basic");
	return plan;
    }
    
    private User createBasicUser() {
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
