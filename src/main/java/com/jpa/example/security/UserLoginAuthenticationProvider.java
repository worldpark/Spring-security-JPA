package com.jpa.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jpa.example.entity.UserDetailsVO;
import com.jpa.example.entity.UserEntity;
import com.jpa.example.repository.UserRepository;

@Service("userLoginAuthenticationProvider")
public class UserLoginAuthenticationProvider implements AuthenticationProvider {
	

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	UserDetailsService userdetailService;
	
	@Autowired
	private PasswordEncoder bCrPasswordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		/* ����ڰ� �Է��� ���� */
		String userId = authentication.getName();
		String userPw = (String) authentication.getCredentials();

		/* DB���� ������ ���� (Ŀ���͸���¡ ����) */
		UserDetailsVO userDetails = (UserDetailsVO) userdetailService
				.loadUserByUsername(userId);

		
		
		/* ���� ���� */
		
		// DB�� ������ ���� ��� ���� �߻� (���̵�/�н����� �߸����� ���� ������ ���� ����)
		// ID �� PW üũ�ؼ� �ȸ��� ��� (matches�� �̿��� ��ȣȭ üũ�� �ؾ���)
		if (userDetails == null || !userId.equals(userDetails.getUsername())
				|| !bCrPasswordEncoder.matches(userPw, userDetails.getPassword())) {

			throw new BadCredentialsException(userId);

		// ���� ���� ������ ������ �ΰ� �޼ҵ� üũ (�̺κе� �ʿ��� �κи� Ŀ���͸���¡ �ϸ� ��)
		// ��� ������ ���
		} else if (!userDetails.isAccountNonLocked()) {
			throw new LockedException(userId);

		// ��Ȱ��ȭ�� ������ ���
		} else if (!userDetails.isEnabled()) {
			throw new DisabledException(userId);

		// ����� ������ ���
		} else if (!userDetails.isAccountNonExpired()) {
			throw new AccountExpiredException(userId);

		// ��й�ȣ�� ����� ���
		} else if (!userDetails.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException(userId);
		}

		// �� ������ �н����� ������ ������ (��ü�� ��� ����ؾ� �ϹǷ�)
		userDetails.setPassword(null);

		/* ���� ���� ��ų ���θ��� Authentication ��ü */
		Authentication newAuth = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());

		return newAuth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
