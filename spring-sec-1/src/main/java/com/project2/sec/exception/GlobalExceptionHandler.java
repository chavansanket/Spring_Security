package com.project2.sec.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException ex){
		return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	
	
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Object> handelInvalidTokenException(InvalidTokenException ex){
		return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
	}
	
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        // Use buildResponse to format the error response
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	
	 /**
     * Build a standard error response structure.
     *
     * @param message The error message to include in the response.
     * @param status  The HTTP status code to use.
     * @return ResponseEntity containing the formatted error response.
     */
	
	private ResponseEntity<Object> buildResponse(String message, HttpStatus status){
		// Create a structured response map
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("message", message);
		response.put("status", status.value());
		System.out.println("hi1");
		System.out.println(response.get(message));
		System.out.println("hi2");
		return new ResponseEntity<>(response, status); //Build and return ResponseEntity
		
	}
	

//example if an invalidTokenException occurs
//	 {
//    "timestamp": "2025-01-05T15:30:45",
//    "message": "JWT token is invalid.",
//    "status": 403
//}
	
}



//@RestControllerAdvice
//public class GlobalExceptionHandler {
//	
//	private ProblemDetail errorDetail;
//	
//	@ExceptionHandler(Exception.class)
//	public ProblemDetail handleSecurityException(Exception ex) {
//		
//		if(ex instanceof BadCredentialsException) {
//			errorDetail = ProblemDetail
//					.forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
//			errorDetail.setProperty("access_denied_reason", "Authentication Failure");
//		}
//		
//		return errorDetail;
//	}
//	
//	
//}