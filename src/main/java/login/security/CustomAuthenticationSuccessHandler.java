package login.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
	    // 로그 추가
	    System.out.println("Authentication 성공! Redirecting to /user/home");

	    // 인증 성공 후 SecurityContext를 설정
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    // 사용자를 홈 페이지로 리다이렉트
	    response.sendRedirect("/user/home");
	}
}
