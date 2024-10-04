package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.AchievementDTO;
import dev.vietis.nampd.employee.achievement.service.AchievementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public String getAllAchievements(Model model) {
        List<AchievementDTO> achievements = achievementService.getAllAchievements();
        model.addAttribute("achievements", achievements);
        return "achievement/list";
    }

    @GetMapping("/{employeeId}")
    public String getAchievementByEmployeeId(@PathVariable Long employeeId, Model model) {
        List<AchievementDTO> achievements = achievementService.getAchievementByEmployeeId(employeeId);
        model.addAttribute("achievement", achievements);
        return "achievement/detail";
    }

    @PostMapping
    public String createAchievement(@ModelAttribute AchievementDTO achievementDto) {
        achievementService.createAchievement(achievementDto);
        return "redirect:/achievements";
    }

    @PostMapping("/{id}/update")
    public String updateAchievement(@PathVariable Long id, @ModelAttribute AchievementDTO achievementDto) {
        achievementService.updateAchievement(id, achievementDto);
        return "redirect:/achievements";
    }

    @PostMapping("/{id}/delete")
    public String deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return "redirect:/achievements";
    }
}

