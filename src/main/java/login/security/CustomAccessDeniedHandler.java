package login.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* ������ �źεǾ��� �� ����ڿ��� ������ �����ϴ� �ڵ鷯 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // sendRedirect�� ����Ͽ� URL�� �����ϰ� /login/login �������� �����̷�Ʈ
    	System.out.println("CustomAccessDeniedHandler 실행!");
        response.sendRedirect(request.getContextPath() + "/login/login?error=access-denied");
    }
}
