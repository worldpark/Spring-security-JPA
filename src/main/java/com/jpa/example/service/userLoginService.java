package com.jpa.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jpa.example.entity.UserDetailsVO;
import com.jpa.example.entity.UserEntity;
import com.jpa.example.repository.UserRepository;

@Service
public class userLoginService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bCrPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetailsVO userDetails = new UserDetailsVO();
		
		UserEntity userEntity = userRepository.selectUserInfoOne(username);
		
		if(userEntity == null) {
			return null;
		}else {
			
			userDetails.setUseranme(userEntity.getId());
			
			userDetails.setPassword(userEntity.getPassword());
			
			userDetails.setAuthorities(userRepository.selectUserAuthOne(username));
		}
		
		return userDetails;
		
	}

}
