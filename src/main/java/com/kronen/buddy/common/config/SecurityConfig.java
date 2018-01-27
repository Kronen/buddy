package com.kronen.buddy.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.kronen.buddy.backend.service.UserSecurityService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private Environment env;
    
    @Autowired
    private UserSecurityService userSecurityService;

    private static final String[] PUBLIC_MATCHERS = { 
	    "/webjars/**", 
	    "/css/**", 
	    "/js/**", 
	    "/images/**", 
	    "/",
	    "/about/**", 
	    "/contact/**", 
	    "/error/**/*",
	    "/h2-console/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
	// Required by h2 console to work
	if(activeProfiles.contains("dev")) {
	    http.csrf().disable();
	    http.headers().frameOptions().disable();
	}
	http
		.authorizeRequests()
		.antMatchers(PUBLIC_MATCHERS).permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").defaultSuccessUrl("/payload")
		.failureUrl("/login?error").permitAll()
		.and()
		.logout().permitAll();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(userSecurityService);	
    }
    
}
