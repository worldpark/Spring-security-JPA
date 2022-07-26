package com.jpa.example.service;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

@Service("userLoginFailHandler")
public class UserLoginFailHandler extends SimpleUrlAuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		
		String errorMessage;
		
		if (exception instanceof AuthenticationServiceException) {
			 errorMessage = "���� ��û�� �źεǾ����ϴ�. �����ڿ��� �����ϼ���.";
		
		} else if(exception instanceof BadCredentialsException) {
			errorMessage = "���̵� �Ǵ� ��й�ȣ�� ���� �ʽ��ϴ�. �ٽ� Ȯ���� �ּ���.";
			
		} else if(exception instanceof LockedException) {
			errorMessage = "������ �Դϴ�.";
			
		} else if(exception instanceof DisabledException) {
			errorMessage = "��Ȱ��ȭ�� �����Դϴ�.";
			
		} else if(exception instanceof AccountExpiredException) {
			errorMessage = "����� �����Դϴ�.";
			
		} else if(exception instanceof CredentialsExpiredException) {
			errorMessage = "��й�ȣ�� ����Ǿ����ϴ�.";
		}else {
			errorMessage = "�� �� ���� ������ �α����� �����߽��ϴ�. �����ڿ��� �����ϼ���.";
		}
		errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		
		setDefaultFailureUrl("/auth/login/?error=true&exception="+errorMessage);
		super.onAuthenticationFailure(request, response, exception);
	}

}
