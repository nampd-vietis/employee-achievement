package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Achievement;
import dev.vietis.nampd.employee.achievement.model.response.PagedResponse;

import java.util.List;

public interface AchievementService {
    Achievement createAchievement(Achievement achievement);

    List<Achievement> getAllAchievements();

//    List<Achievement> getAchievementByEmployeeId(Long employeeId);
    Achievement getAchievementById(Long achievementId);

    Achievement updateAchievement(Long achievementId, Achievement updatedAchievement);

    void deleteAchievement(Long achievementId);

    List<EmployeeAchievementsSumDTO> getEmployeeAchievementsSum();
    List<DepartmentAchievementsSumDTO> getDepartmentAchievementsSum();

    PagedResponse<Achievement> getAchievementsPaginated(int page, int size);
}
