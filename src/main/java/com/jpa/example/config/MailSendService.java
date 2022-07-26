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
	
	//����Ű ���� �߻�
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
	
	
	//����Ű ����
	private String GetKey(boolean lowerCheck, int size) {
		this.lowerCheck = lowerCheck;
		this.size = size;
		return getAuthCode();
	}
	
	//�������� ������
	public String mailSendWithUserKey(String email, String member_id, HttpServletRequest request) {
		
		
		//6�ڸ� ������ȣ ����
		String authKey = GetKey(false, 6);
		
		
		MimeMessage mail = mailSender.createMimeMessage();
		String mailContent ="<h2>�ȳ��ϼ��� �������� �Դϴ�.</h2><br><br>" 
				+ "<h3>" + member_id + "��</h3>" + "<p>�����ϱ� ��ư�� �����ø� �α����� �Ͻ� �� �ֽ��ϴ� : " 
				+ "<a href='http://localhost:8080" + request.getContextPath() + "/user/key_alter?user_id="+ member_id +"'>�����ϱ�</a></p>"
				+ "(Ȥ�� �߸� ���޵� �����̶�� �� �̸����� �����ϼŵ� �˴ϴ�)";

		
		//�������� ������
		try {
			mail.setSubject("ȸ������ �̸��� ����", "utf-8");
			mail.setText(mailContent, "utf-8", "html");
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			mailSender.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return authKey;
	}
	
	//���� ����
	public void alter_userKey_service(String user_id) {
		UserEntity userEntity = userRepository.selectUserInfoOne(user_id);
		userEntity.setAuth("ROLE_OUSER");
		userRepository.save(userEntity);
	}

}
