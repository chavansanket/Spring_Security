package com.project2.sec.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project2.sec.exception.InvalidTokenException;
import com.project2.sec.exception.TokenExpiredException;
import com.project2.sec.exception.UserNotFoundException;
import com.project2.sec.service.JWTService;
import com.project2.sec.service.MyUserDetailsService;

import ch.qos.logback.core.joran.conditional.ElseAction;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTService jwtService;  // A service to validate and parse JWTs
	
	@Autowired
	ApplicationContext context;
	
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	        throws ServletException, IOException {
//	    String authHeader = request.getHeader("Authorization");
//	    String token = null;
//	    String username = null;
//
//	    try {
//	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//	            token = authHeader.substring(7);
//	            username = jwtService.extractUserName(token);
//	        } else if (authHeader == null) {
//	            throw new InvalidTokenException("Authorization header is missing.");
//	        } else {
//	            throw new InvalidTokenException("Authorization header must start with 'Bearer '.");
//	        }
//
//	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//	            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
//
//	            if (jwtService.validateToken(token, userDetails)) {
//	                UsernamePasswordAuthenticationToken authToken =
//	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//	                SecurityContextHolder.getContext().setAuthentication(authToken);
//	            } else if (jwtService.isTokenExpired(token)) {
//	                throw new TokenExpiredException("JWT token has expired.");
//	            } else {
//	                throw new InvalidTokenException("JWT token is invalid.");
//	            }
//	        }
//
//	        // Continue the filter chain
//	        filterChain.doFilter(request, response);
//	    } catch (InvalidTokenException | TokenExpiredException | UserNotFoundException e) {
//	        handleException(response, e);
//	    }
//	}
//
//	/**
//	 * Writes the error response to the client.
//	 */
//	private void handleException(HttpServletResponse response, Exception e) throws IOException {
//	    response.setContentType("application/json");
//	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 for authentication issues
//	    response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
//	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhdm5pIiwiaWF0IjoxNzM2MDQ1NDYyLCJleHAiOjE3MzYwNDU1NzB9.v_J5Z6zhb_LBqOAZXxag95iuy1ri-_VEhwIBuXPYay4
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		 // Extract token from Authorization header
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			try {
				username = jwtService.extractUserName(token);
				
			}catch (Exception e) {
				throw new InvalidTokenException("Invalid JWT token.");
			}
		}
		
		// Validate the extracted username and token
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails;
			
			try {				
				userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
			} catch (Exception e) {
				throw new UserNotFoundException("User not found in the database.");
			}
			
//			if(jwtService.validateToken(token, userDetails)) {
//				
//				UsernamePasswordAuthenticationToken authToken =
//						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				
//				SecurityContextHolder.getContext().setAuthentication(authToken);
//			}else if (jwtService.isTokenExpired(token)) {
//				throw new TokenExpiredException("JWT token is expired.");
//			}else {
//				throw new InvalidTokenException("JWT token is invalid.");
//			}
			
			if (jwtService.isTokenExpired(token)) {
	            throw new TokenExpiredException("JWT token is expired.");
	        } else if (!jwtService.validateToken(token, userDetails)) {
	            throw new InvalidTokenException("JWT token is invalid.");
	        }

	        // Create authentication token if everything is valid
	        UsernamePasswordAuthenticationToken authToken =
	                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	        SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		
		filterChain.doFilter(request, response);
		
	}

}
