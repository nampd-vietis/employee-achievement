package dev.vietis.nampd.employee.achievement.auth;

import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");

        // Kiểm tra nếu chưa đăng nhập
        if (employee == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Kiểm tra quyền hạn với URL hiện tại
        String uri = request.getRequestURI();
        if (uri.startsWith("/admin") && employee.getRole() != Employee.Role.ADMIN) {
            response.sendRedirect("/access-denied");
            return false;
        }

        return true;
    }
}
