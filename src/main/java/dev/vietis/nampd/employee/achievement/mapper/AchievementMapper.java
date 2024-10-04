package dev.vietis.nampd.employee.achievement.mapper;

import dev.vietis.nampd.employee.achievement.model.dto.AchievementDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Achievement;
import org.springframework.stereotype.Service;

@Service
public class AchievementMapper {
    //map achievement -> dto
    public AchievementDTO toAchievementDto(Achievement achievement) {
        return new AchievementDTO(
                achievement.getId(),
                achievement.getType(),
                achievement.getReason(),
                achievement.getDateRecorded(),
                achievement.getEmployee().getId()
        );
    }

    //map dto ->
    public Achievement toAchievement(AchievementDTO achievementDto) {
        if (achievementDto == null) {
            return null;
        }

        Achievement achievement = new Achievement();

        achievement.setId(achievementDto.getId());
        achievement.setType(achievementDto.getType());
        achievement.setReason(achievementDto.getReason());
        achievement.setDateRecorded(achievementDto.getDateRecorded());

        return achievement;
    }
}
