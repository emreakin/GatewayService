package com.tesodev.gateway.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Value("${jwt.username}")
	private String username;
	
	@Value("${jwt.password}")
	private String password;

	@Override
	public UserDetails loadUserByUsername(String loginUsername) throws UsernameNotFoundException {
		if (username.equals(loginUsername)) {
			return new User(username, password, 
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + loginUsername);
		}
	}

}