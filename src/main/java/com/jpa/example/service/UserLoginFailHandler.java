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
			 errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
		
		} else if(exception instanceof BadCredentialsException) {
			errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
			
		} else if(exception instanceof LockedException) {
			errorMessage = "잠긴계정 입니다.";
			
		} else if(exception instanceof DisabledException) {
			errorMessage = "비활성화된 계정입니다.";
			
		} else if(exception instanceof AccountExpiredException) {
			errorMessage = "만료된 계정입니다.";
			
		} else if(exception instanceof CredentialsExpiredException) {
			errorMessage = "비밀번호가 만료되었습니다.";
		}else {
			errorMessage = "알 수 없는 이유로 로그인이 실패했습니다. 관리자에게 문의하세요.";
		}
		errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		
		setDefaultFailureUrl("/auth/login/?error=true&exception="+errorMessage);
		super.onAuthenticationFailure(request, response, exception);
	}

}
