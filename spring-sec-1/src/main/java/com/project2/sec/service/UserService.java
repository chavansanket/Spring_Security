package com.project2.sec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project2.sec.model.User;
import com.project2.sec.repo.UserRepo;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public User registerUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
		return user;
	}

}
