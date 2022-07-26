package com.jpa.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.example.entity.UserEntity;
import com.jpa.example.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bCrPasswordEncoder;
	
	public UserEntity regist(UserEntity userEntity) throws Exception{
		if(this.isEmailExist(userEntity.getId())) {
			throw new Exception("이미 등록된 아이디");
		}
		else {
			UserEntity newUser = userEntity;
			newUser.hashPassword(bCrPasswordEncoder);
			newUser.setAuth("ROLE_NUSER");
			
			return userRepository.save(newUser);
		}
	}
	
	//아이디 중복여부
	public boolean isEmailExist(String id) {
		int byId = userRepository.findbyId(id);
		boolean check;
		
		if(byId >= 1) {
			check = true;
		}
		else
		{
			check = false;
		}
		
		return check;
	}
	
	public UserEntity infoCheck(String id) {
		
		return userRepository.selectUserInfoOne(id);
	}

}
