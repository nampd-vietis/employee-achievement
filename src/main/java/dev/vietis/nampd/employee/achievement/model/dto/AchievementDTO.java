package dev.vietis.nampd.employee.achievement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDTO {
    private Long id;
    private Byte type;
    private String reason;
    private LocalDate dateRecorded;
    private Long employeeId;
}
