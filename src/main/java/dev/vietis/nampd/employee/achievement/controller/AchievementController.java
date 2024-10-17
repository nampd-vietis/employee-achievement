package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Achievement;
import dev.vietis.nampd.employee.achievement.service.AchievementService;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin/achievements")
public class AchievementController {

    private final AchievementService achievementService;
    private final EmployeeService employeeService;

    public AchievementController(AchievementService achievementService, EmployeeService employeeService) {
        this.achievementService = achievementService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllAchievements(Model model) {
        List<Achievement> achievements = achievementService.getAllAchievements();
        model.addAttribute("achievements", achievements);
        return "achievement/list";
    }

    // Hiển thị form tạo mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("achievement", new Achievement());

        // Thêm danh sách nv vào model
        List<EmployeeDTO> employeeDTOS = employeeService.getAllEmployees();
        model.addAttribute("employees", employeeDTOS);

        return "achievement/add_form";
    }

    @PostMapping
    public String createAchievement(@ModelAttribute Achievement achievement) {
        achievementService.createAchievement(achievement);
        return "redirect:/admin/achievements";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            Achievement achievement = achievementService.getAchievementById(id);
            model.addAttribute("achievement", achievement);

            // Thêm danh sách nv vào model
            List<EmployeeDTO> employeeDTOS = employeeService.getAllEmployees();
            model.addAttribute("employees", employeeDTOS);

            return "achievement/update_form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Thành tích không tồn tại");
            return "error";
        }
    }

    @PostMapping("/update/{id}")
    public String updateAchievement(@PathVariable Long id,
                                   @ModelAttribute("achievement") Achievement achievement,
                                   Model model) {
        try {
            achievementService.updateAchievement(id, achievement);
            return "redirect:/admin/achievements";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Achievement không tồn tại");
            return "achievement/update_form";
        }
    }

    // Xóa phòng ban
    @PostMapping("/delete/{id}")
    public String deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return "redirect:/admin/achievements";
    }

    //tổng hợp nhân viên
    @GetMapping("/employee")
    public String viewEmployeeAchievements(Model model) {
        List<EmployeeAchievementsSumDTO> employeeSum = achievementService.getEmployeeAchievementsSum();
        model.addAttribute("employeeSum", employeeSum);
        return "achievement/employee_achievement";
    }

    //tổng hợp phòng ban
    @GetMapping("/department")
    public String viewDepartmentAchievements(Model model) {
        List<DepartmentAchievementsSumDTO> departmentSum = achievementService.getDepartmentAchievementsSum();
        model.addAttribute("departmentSum", departmentSum);
        return "achievement/department_achievement";
    }
}

