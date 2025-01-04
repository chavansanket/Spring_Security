package com.project2.sec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project2.sec.model.User;
import com.project2.sec.model.UserPrincipal;
import com.project2.sec.repo.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u =  userRepo.findByUsername(username);
		
		if(u == null) {
			System.out.println("User Not Found");
			throw new UsernameNotFoundException(username);
		}
		
		return new UserPrincipal(u);
		
	}
	
	

}
