package CSDL.spring_ml_practice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private void sendJsonErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseMap));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("memberEmail") == null) {
            String requestUri = request.getRequestURI();
            if (requestUri.startsWith("/api")) {
                sendJsonErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다."); // 401 Unauthorized
            } else {
                response.sendRedirect("/sign_in");
            }
            return false;
        }
        return true;
    }
}
