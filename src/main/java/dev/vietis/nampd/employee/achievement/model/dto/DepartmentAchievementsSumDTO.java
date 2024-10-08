package dev.vietis.nampd.employee.achievement.model.dto;

import dev.vietis.nampd.employee.achievement.model.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentAchievementsSumDTO {
    private Department department;
    private long totalAchievements;
    private long totalDisciplines;
    private int rewardPoints;
}
