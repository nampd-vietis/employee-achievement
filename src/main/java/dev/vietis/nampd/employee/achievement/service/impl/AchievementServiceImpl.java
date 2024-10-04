package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.mapper.AchievementMapper;
import dev.vietis.nampd.employee.achievement.model.dto.AchievementDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Achievement;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.AchievementRepository;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import dev.vietis.nampd.employee.achievement.service.AchievementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final EmployeeRepository employeeRepository;

    public AchievementServiceImpl(AchievementRepository achievementRepository, AchievementMapper achievementMapper, EmployeeRepository employeeRepository) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AchievementDTO createAchievement(AchievementDTO achievementDto) {
        Employee employee = employeeRepository.findById(achievementDto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Achievement achievement = achievementMapper.toAchievement(achievementDto);
        achievement.setEmployee(employee);

        return achievementMapper.toAchievementDto(achievementRepository.save(achievement));
    }

    @Override
    public List<AchievementDTO> getAllAchievements() {
        return achievementRepository.findAll().stream()
                .map(achievementMapper::toAchievementDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AchievementDTO> getAchievementByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        List<Achievement> achievements = achievementRepository.findByEmployeeId(employeeId);

        return achievements.stream()
                        .map(achievementMapper::toAchievementDto).
                        collect(Collectors.toList());
    }

    @Override
    public AchievementDTO updateAchievement(Long achievementId, AchievementDTO updatedAchievementDTO) {
        Achievement existingAchievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new NoSuchElementException("Achievement not found"));

        existingAchievement.setType(updatedAchievementDTO.getType());
        existingAchievement.setReason(updatedAchievementDTO.getReason());
        existingAchievement.setDateRecorded(updatedAchievementDTO.getDateRecorded());

        Employee employee = employeeRepository.findById(updatedAchievementDTO.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        existingAchievement.setEmployee(employee);

        return achievementMapper.toAchievementDto(achievementRepository.save(existingAchievement));
    }

    @Override
    public void deleteAchievement(Long achievementId) {
        if (!achievementRepository.existsById(achievementId)) {
            throw new NoSuchElementException("Achievement not found");
        }
        achievementRepository.deleteById(achievementId);
    }
}
