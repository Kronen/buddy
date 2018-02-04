package com.kronen.buddy;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.backend.service.UserService;
import com.kronen.buddy.common.enums.PlansEnum;
import com.kronen.buddy.common.enums.RolesEnum;
import com.kronen.buddy.common.utils.UserUtils;

@SpringBootApplication
@PropertySource("file:///${user.home}/.buddy/application-common.properties")
public class BuddyApplication implements CommandLineRunner {
        
    private static final Logger LOG = LoggerFactory.getLogger(BuddyApplication.class);
    
    @Autowired
    public UserService userService;
    
    @Value("${webmaster.username}")
    private String webmasterUsername;
    
    @Value("${webmaster.password}")
    private String webmasterPassword;
    
    @Value("${webmaster.email}")
    private String webmasterEmail;

    public static void main(String[] args) {
	SpringApplication.run(BuddyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
		
	User user = UserUtils.createBasicUser(webmasterUsername, webmasterEmail);
	user.setPassword(webmasterPassword);
	Set<UserRole> userRoles = new HashSet<>();
	userRoles.add(new UserRole(user, new Role(RolesEnum.ADMIN)));
	LOG.debug("Creating user with username {}", user.getUsername());
	userService.createUser(user, PlansEnum.PRO, userRoles);
	LOG.info("User {} created", user.getUsername());
    }
}
