package com.project2.sec.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
	
	@GetMapping("/")
	public String getHi() {
		
		return "Hi from contoller";
	}

}
