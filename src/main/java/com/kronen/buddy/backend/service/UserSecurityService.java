package com.kronen.buddy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.repositories.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	User user = userRepository.findByUsername(username);
	if(user == null) {
	    throw new UsernameNotFoundException("Username " + username + " not found");
	}
	return user;
    }
    
}
