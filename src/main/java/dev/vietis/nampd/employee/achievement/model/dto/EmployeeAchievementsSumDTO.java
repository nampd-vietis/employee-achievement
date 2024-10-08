package dev.vietis.nampd.employee.achievement.model.dto;

import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAchievementsSumDTO {
    private Employee employee;
    private long totalAchievements;
    private long totalDisciplines;
    private int rewardPoints;
}
