package com.kronen.buddy.backend.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.persistence.repositories.PlanRepository;
import com.kronen.buddy.backend.persistence.repositories.RoleRepository;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;
import com.kronen.buddy.common.enums.PlansEnum;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles) {
        String encriptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encriptedPassword);

        Plan plan = new Plan(plansEnum);
        if(!planRepository.existsById(plansEnum.getId())) {
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);
        for(UserRole userRole : userRoles) {
            Role role = roleRepository.save(userRole.getRole());
            userRole.setRole(role);
        }

        user.getUserRoles().addAll(userRoles);

        userRepository.save(user);

        return user;
    }

    public void updateUserPassword(Long userId, String password) {
        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);
        LOG.debug("Password updated successfully for user id {]", userId);
    }
}
