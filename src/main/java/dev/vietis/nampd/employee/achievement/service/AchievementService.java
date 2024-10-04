package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.dto.AchievementDTO;

import java.util.List;

public interface AchievementService {
    AchievementDTO createAchievement(AchievementDTO achievementDto);

    List<AchievementDTO> getAllAchievements();

    List<AchievementDTO> getAchievementByEmployeeId(Long employeeId);

    AchievementDTO updateAchievement(Long achievementId, AchievementDTO updatedAchievementDTO);

    void deleteAchievement(Long achievementId);
}
