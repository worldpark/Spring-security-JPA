package com.jpa.example.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jpa.example.config.MailSendService;
import com.jpa.example.entity.UserDetailsVO;
import com.jpa.example.entity.UserEntity;
import com.jpa.example.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService userservice = new UserService();
	
	@Autowired
	MailSendService mailservice = new MailSendService();
	
	@RequestMapping("/idcheck")
	@ResponseBody
	public Map<Object, Object> idcheck(@RequestBody String id){
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		if(userservice.isEmailExist(id)){
			map.put("cnt", 1);
		}
		else {
			map.put("cnt", 0);
		}
		
		return map;
	}
	
	@RequestMapping(value="/memberRegist", method = RequestMethod.POST)
	@ResponseBody
	public Map memberRegist(@RequestBody UserEntity user) throws Exception {
		
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("status", 1);
		
		userservice.regist(user);
		return map;
	}
	
	@RequestMapping("/emailcheck")
	@ResponseBody
	public int emailcheck(@RequestBody String email) {
		
		return 1;
	}

	@RequestMapping("/infoCheck")
	@ResponseBody
	public String infoCheck(Authentication authentication) {
		
		UserDetailsVO userVo = (UserDetailsVO) authentication.getPrincipal();
		
		UserEntity userEntity = userservice.infoCheck(userVo.getUsername());
		
		
		return "suc";
	}
	
	@RequestMapping(value = "/auth/login", method = RequestMethod.GET)
	public String loginFail(@RequestParam(value="error", required=false) String error,
							@RequestParam(value="exception", required=false) String exception,
							Model model) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		
		return "Login";
	}

	@RequestMapping("/emailAuth")
	@ResponseBody
	public String emailAuth(Authentication authentication, HttpServletRequest request) {
		
		UserDetailsVO userVo = (UserDetailsVO) authentication.getPrincipal();
		
		UserEntity userEntity = userservice.infoCheck(userVo.getUsername());
		
		
		mailservice.mailSendWithUserKey(userEntity.getEmail(), userEntity.getId(), request);
		
		return "suc";
	}
	

	//이메일인증완료
	@RequestMapping(value = "/user/key_alter", method = RequestMethod.GET)
	public String key_alterConfirm(@RequestParam("user_id") String user_id) {
		
		mailservice.alter_userKey_service(user_id);
		
		return "Login";
	}
}
