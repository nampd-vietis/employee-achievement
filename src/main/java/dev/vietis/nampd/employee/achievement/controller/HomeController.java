package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.service.AchievementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final AchievementService achievementService;

    public HomeController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping("/home")
    public String showTopEmployee(Model model) {
        List<EmployeeAchievementsSumDTO> employeeAchievements = achievementService.getEmployeeAchievementsSum();

        // Sắp xếp theo điểm thưởng giảm dần và lấy 10 nhân viên đầu tiên
        List<EmployeeAchievementsSumDTO> topEmployees = employeeAchievements.stream()
                .sorted(Comparator.comparingInt(EmployeeAchievementsSumDTO::getRewardPoints).reversed())
                .limit(10)
                .collect(Collectors.toList());

        model.addAttribute("topEmployees", topEmployees);
        return "admin-home";
    }
}
