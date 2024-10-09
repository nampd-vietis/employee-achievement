package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.service.AchievementService;
import dev.vietis.nampd.employee.achievement.service.FileStorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final AchievementService achievementService;
    private final FileStorageService fileStorageService;

    public HomeController(AchievementService achievementService, FileStorageService fileStorageService) {
        this.achievementService = achievementService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        Resource file = fileStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"anh" + "\"")
                .body(file);
    }

    @GetMapping("/admin/home")
    public String showAdminHome(HttpSession session, Model model) {

        List<EmployeeAchievementsSumDTO> employeeAchievements = achievementService.getEmployeeAchievementsSum();
        List<EmployeeAchievementsSumDTO> topEmployees = employeeAchievements.stream()
                .sorted(Comparator.comparingInt(EmployeeAchievementsSumDTO::getRewardPoints).reversed())
                .limit(10)
                .collect(Collectors.toList());

        model.addAttribute("topEmployees", topEmployees);
        model.addAttribute("adminName", session.getAttribute("loggedInEmployee"));
        return "admin-home";
    }

    @GetMapping("/user/home")
    public String showUserHome(HttpSession session, Model model) {

        List<EmployeeAchievementsSumDTO> employeeAchievements = achievementService.getEmployeeAchievementsSum();
        List<EmployeeAchievementsSumDTO> topEmployees = employeeAchievements.stream()
                .sorted(Comparator.comparingInt(EmployeeAchievementsSumDTO::getRewardPoints).reversed())
                .limit(10)
                .collect(Collectors.toList());

        model.addAttribute("topEmployees", topEmployees);
        model.addAttribute("userName", session.getAttribute("loggedInEmployee"));
        return "user-home";
    }
}
