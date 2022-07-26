package com.jpa.example.config;

import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.jpa.example.entity.UserEntity;
import com.jpa.example.repository.UserRepository;

@Service
public class MailSendService {
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Autowired
	UserRepository userRepository;
	
	private int size;
	private boolean lowerCheck;
	
	//인증키 난수 발생
	private String getAuthCode() {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		int num = 0;
		
		while(buffer.length() < size) {
			num = random.nextInt(10);
			buffer.append(num);
		}
		
		return buffer.toString();
	}
	
	
	//인증키 생성
	private String GetKey(boolean lowerCheck, int size) {
		this.lowerCheck = lowerCheck;
		this.size = size;
		return getAuthCode();
	}
	
	//인증메일 보내기
	public String mailSendWithUserKey(String email, String member_id, HttpServletRequest request) {
		
		
		//6자리 인증번호 생성
		String authKey = GetKey(false, 6);
		
		
		MimeMessage mail = mailSender.createMimeMessage();
		String mailContent ="<h2>안녕하세요 인증메일 입니다.</h2><br><br>" 
				+ "<h3>" + member_id + "님</h3>" + "<p>인증하기 버튼을 누르시면 로그인을 하실 수 있습니다 : " 
				+ "<a href='http://localhost:8080" + request.getContextPath() + "/user/key_alter?user_id="+ member_id +"'>인증하기</a></p>"
				+ "(혹시 잘못 전달된 메일이라면 이 이메일을 무시하셔도 됩니다)";

		
		//인증메일 보내기
		try {
			mail.setSubject("회원가입 이메일 인증", "utf-8");
			mail.setText(mailContent, "utf-8", "html");
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			mailSender.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return authKey;
	}
	
	//메일 인증
	public void alter_userKey_service(String user_id) {
		UserEntity userEntity = userRepository.selectUserInfoOne(user_id);
		userEntity.setAuth("ROLE_OUSER");
		userRepository.save(userEntity);
	}

}
