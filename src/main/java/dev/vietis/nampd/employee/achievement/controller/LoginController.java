package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeLoginDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute EmployeeLoginDTO employeeLoginDTO,
                        Model model,
                        HttpSession session) {
        try {
            Employee employee = loginService.login(employeeLoginDTO.getEmail(), employeeLoginDTO.getPassword());

            session.setAttribute("loggedInEmployee", employee);

            if (employee.getRole() == Employee.Role.ADMIN) {
                return "redirect:/admin/home";
            } else {
                return "redirect:/user/home";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", "Email hoặc mật khẩu không chính xác");
            return "login";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa session
        return "redirect:/login";
    }
}
