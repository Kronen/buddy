package com.kronen.buddy.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.persistence.repositories.PlanRepository;
import com.kronen.buddy.backend.persistence.repositories.RoleRepository;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;
import com.kronen.buddy.common.enums.PlansEnum;
import com.kronen.buddy.common.enums.RolesEnum;
import com.kronen.buddy.common.utils.UserUtils;

public abstract class AbstractIntegrationTest {

    @Autowired
    protected PlanRepository planRepository;
    
    @Autowired
    protected RoleRepository roleRepository;
    
    @Autowired
    protected UserRepository userRepository;
    
    protected Role createRole(RolesEnum rolesEnum) {
	return new Role(rolesEnum);
    }

    protected Plan createPlan() {
	return new Plan(PlansEnum.BASIC);
    }
    
    protected User createUser(String username, String email) {
	Plan basicPlan = createPlan();
	basicPlan = planRepository.save(basicPlan);
	
	User basicUser = UserUtils.createBasicUser(username, email);
	basicUser.setPlan(basicPlan);
	
	Role basicRole = createRole(RolesEnum.BASIC);
	roleRepository.save(basicRole);
	
	Set<UserRole> userRoles = new HashSet<>();
	UserRole userRole = new UserRole(basicUser, basicRole);
	userRoles.add(userRole);
	
	basicUser.getUserRoles().addAll(userRoles);
	basicUser = userRepository.save(basicUser);
	
	return basicUser;
    }
    
    protected User createUser(TestName testName) {
	return createUser(testName.getMethodName(), testName.getMethodName() + "@buddy.com");
    }
    
}
