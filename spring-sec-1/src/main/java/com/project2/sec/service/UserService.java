package com.project2.sec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project2.sec.model.User;
import com.project2.sec.repo.UserRepo;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	AuthenticationManager authManager;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public User registerUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return user;
	}

	public String verify(User user) {
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if(authentication.isAuthenticated())
			return jwtService.generateToken(user.getUsername());
		
		return "Fail";

	}
	

}
