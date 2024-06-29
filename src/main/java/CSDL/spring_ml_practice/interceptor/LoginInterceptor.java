package CSDL.spring_ml_practice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("memberEmail") == null) {
            String ajaxHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            } else {
                response.sendRedirect("/sign_in");
            }
            return false;
        }
        return true;
    }
}
