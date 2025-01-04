package com.project2.sec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
				//disable csrf
				.csrf(customizer->customizer.disable())
				.authorizeHttpRequests(request->request.anyRequest().authenticated())
				////form login
//				http.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//suppose if there was a form login and if you tries from browser, 
//				to do login, after login same page will come because no session is maintained 
				.build();
	}
	
////custom UserDetailsService //we changed with default // but data is hard coded here 
//  @Bean
//  UserDetailsService userDetailsService() {
//
//      UserDetails user1 = User
//              .withDefaultPasswordEncoder()
//              .username("kiran")
//              .password("k@123")
//              .roles("USER")
//              .build();
//
//      UserDetails user2 = User
//              .withDefaultPasswordEncoder()
//              .username("harsh")
//              .password("h@123")
//              .roles("ADMIN")
//              .build();
//      return new InMemoryUserDetailsManager(user1, user2);
//  }

	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		
		return provider;
	}

}
