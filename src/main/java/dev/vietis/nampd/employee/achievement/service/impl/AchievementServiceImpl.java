package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.exception.AppException;
import dev.vietis.nampd.employee.achievement.exception.ErrorCode;
import dev.vietis.nampd.employee.achievement.model.dto.DepartmentAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Achievement;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.model.response.PagedResponse;
import dev.vietis.nampd.employee.achievement.repository.AchievementRepository;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import dev.vietis.nampd.employee.achievement.service.AchievementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public AchievementServiceImpl(AchievementRepository achievementRepository, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.achievementRepository = achievementRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Achievement createAchievement(Achievement achievement) {
        Employee employee = employeeRepository.findById(achievement.getEmployee().getId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        achievement.setEmployee(employee);

        return achievementRepository.save(achievement);
    }

    @Override
    public List<Achievement> getAllAchievements() {
        return new ArrayList<>(achievementRepository.findAll());
    }

//    @Override
//    public List<Achievement> getAchievementByEmployeeId(Long employeeId) {
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
//
//        List<Achievement> achievements = achievementRepository.findByEmployeeId(employeeId);
//
//        return new ArrayList<>(achievements);
//    }

    @Override
    public Achievement getAchievementById(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.ACHIEVEMENT_NOT_FOUND));
    }

    @Override
    public Achievement updateAchievement(Long achievementId, Achievement updatedAchievement) {
        Achievement existingAchievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new AppException(ErrorCode.ACHIEVEMENT_NOT_FOUND));

        existingAchievement.setType(updatedAchievement.getType());
        existingAchievement.setReason(updatedAchievement.getReason());
        existingAchievement.setDateRecorded(updatedAchievement.getDateRecorded());

        Employee employee = employeeRepository.findById(updatedAchievement.getEmployee().getId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        existingAchievement.setEmployee(employee);

        return achievementRepository.save(existingAchievement);
    }

    @Override
    public void deleteAchievement(Long achievementId) {
        if (!achievementRepository.existsById(achievementId)) {
            throw new AppException(ErrorCode.ACHIEVEMENT_NOT_FOUND);
        }
        achievementRepository.deleteById(achievementId);
    }

    @Override
    public List<EmployeeAchievementsSumDTO> getEmployeeAchievementsSum() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> {
                    long totalAchievements = achievementRepository.countByEmployeeAndType(employee, (byte) 1);
                    long totalDisciplines = achievementRepository.countByEmployeeAndType(employee, (byte) 0);
                    int rewardPoints = calculateRewardPoints(totalAchievements, totalDisciplines);
                    return new EmployeeAchievementsSumDTO(employee, totalAchievements, totalDisciplines, rewardPoints);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentAchievementsSumDTO> getDepartmentAchievementsSum() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(department -> {
                    long totalAchievements = achievementRepository.countByEmployeeDepartmentAndType(department, (byte) 1);
                    long totalDisciplines = achievementRepository.countByEmployeeDepartmentAndType(department, (byte) 0);
                    int rewardPoints = calculateRewardPoints(totalAchievements, totalDisciplines);
                    return new DepartmentAchievementsSumDTO(department, totalAchievements, totalDisciplines, rewardPoints);
                })
                .collect(Collectors.toList());
    }

    private int calculateRewardPoints(long totalAchievements, long totalDisciplines) {
        return (int) (totalAchievements - totalDisciplines);
    }

    @Override
    public PagedResponse<Achievement> getAchievementsPaginated(int page, int size) {
        Page<Achievement> achievementPage = achievementRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id")));

        return new PagedResponse<>(
                achievementPage.getContent(),
                achievementPage.getNumber(),
                achievementPage.getSize(),
                achievementPage.getTotalElements(),
                achievementPage.getTotalPages()
        );
    }
}
