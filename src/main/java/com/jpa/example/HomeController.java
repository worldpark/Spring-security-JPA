package com.jpa.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jpa.example.entity.UserDetailsVO;
import com.jpa.example.entity.UserEntity;
import com.jpa.example.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	

	@Autowired
	UserService userservice = new UserService();
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		
		return "Login";
	}
	
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String regist() {
		
		return "regist";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error() {
		
		return "accessDenied";
	}
	
	@RequestMapping(value = "/loginsuc", method = RequestMethod.GET)
	public String loginsuc() {
		
		return "Loginsuccess";
	}
	
}
