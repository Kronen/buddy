package com.kronen.buddy.test.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.persistence.repositories.PlanRepository;
import com.kronen.buddy.backend.persistence.repositories.RoleRepository;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;
import com.kronen.buddy.common.enums.PlansEnum;
import com.kronen.buddy.common.enums.RolesEnum;
import com.kronen.buddy.common.utils.UsersUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoriesIntegrationTest {

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
	Plan basicPlan = createPlan();
	planRepository.save(basicPlan);
	Optional<Plan> retrievedPlan = planRepository.findById(PlansEnum.BASIC.getId());
	assertThat(retrievedPlan.isPresent()).isTrue();
    }
    
    @Test
    public void createNewRole() {
	Role basicRole = createRole(RolesEnum.BASIC);
	roleRepository.save(basicRole);
	Optional<Role> retrievedRole = roleRepository.findById(RolesEnum.BASIC.getId());
	assertThat(retrievedRole.isPresent()).isTrue();
    }
    
    @Test
    @Transactional
    public void createNewUser() {
	Plan basicPlan = createPlan();
	basicPlan = planRepository.save(basicPlan);
	
	User basicUser = UsersUtils.createBasicUser();
	basicUser.setPlan(basicPlan);
	
	Role basicRole = createRole(RolesEnum.BASIC);
	
	Set<UserRole> userRoles = new HashSet<>();
	UserRole userRole = new UserRole(basicUser, basicRole);
	userRoles.add(userRole);
	
	basicUser.getUserRoles().addAll(userRoles);
	
	for(UserRole ur : userRoles) {
	    roleRepository.save(ur.getRole());
	}
	
	basicUser = userRepository.save(basicUser);
	
	Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
	assertThat(newlyCreatedUser.isPresent()).isTrue();
	assertThat(newlyCreatedUser.get().getId() != 0).isTrue();
	assertThat(newlyCreatedUser.get().getPlan()).isNotNull();
	Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.get().getUserRoles();
	assertThat(newlyCreatedUserUserRoles).isNotNull();
	for(UserRole ur : newlyCreatedUserUserRoles) {
	    assertThat(ur.getRole()).isNotNull();
	    assertThat(ur.getRole().getId()).isNotNull();
	}
	
	
    }

    private Role createRole(RolesEnum rolesEnum) {
	return new Role(rolesEnum);
    }

    private Plan createPlan() {
	return new Plan(PlansEnum.BASIC);
    }
    
}
