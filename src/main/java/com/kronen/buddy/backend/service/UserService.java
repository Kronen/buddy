package com.kronen.buddy.backend.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.persistence.repositories.PlanRepository;
import com.kronen.buddy.backend.persistence.repositories.RoleRepository;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;
import com.kronen.buddy.common.enums.PlansEnum;

@Service
@Transactional(readOnly = true)
public class UserService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles) {
	Plan plan = new Plan(plansEnum);
	if(!planRepository.existsById(plansEnum.getId())) {
	    plan = planRepository.save(plan);
	}
	
	user.setPlan(plan);
	for(UserRole userRole : userRoles) {
	    roleRepository.save(userRole.getRole());
	}
	
	user.getUserRoles().addAll(userRoles);
	
	userRepository.save(user);
	
	return user;	
    }
}
