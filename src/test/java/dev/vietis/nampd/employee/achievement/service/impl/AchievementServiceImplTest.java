package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeAchievementsSumDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Achievement;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.AchievementRepository;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AchievementServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private AchievementServiceImpl achievementServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAchievementSuccess() {
        Employee employee = new Employee();
        employee.setId(1L);

        Achievement achievement = new Achievement();
        achievement.setEmployee(employee);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(achievementRepository.save(achievement)).thenReturn(achievement);

        Achievement createdAchievement = achievementServiceImpl.createAchievement(achievement);

        assertEquals(employee, createdAchievement.getEmployee());
        verify(employeeRepository, times(1)).findById(1l);
        verify(achievementRepository,times(1)).save(achievement);

    }

    //not found employee
    @Test
    void createAchievementFail() {
        Employee employee = new Employee();
        employee.setId(1L);

        Achievement achievement = new Achievement();
        achievement.setEmployee(employee);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            achievementServiceImpl.createAchievement(achievement);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(achievementRepository, never()).save(any());

    }

    @Test
    void getAllAchievements() {
        Achievement achievement = new Achievement();
        Achievement achievement1 = new Achievement();

        when(achievementRepository.findAll()).thenReturn(List.of(achievement, achievement1));

        List<Achievement> achievements = achievementServiceImpl.getAllAchievements();

        assertEquals(2, achievements.size());
        assertEquals(achievement, achievements.get(0));
        assertEquals(achievement1, achievements.get(1));
    }

    @Test
    void getAchievementByIdSuccess() {
        Long achievementId = 1L;
        Achievement achievement = new Achievement();

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        Achievement resultAchievement = achievementServiceImpl.getAchievementById(achievementId);

        assertEquals(achievement, resultAchievement);
    }

    @Test
    void getAchievementByIdNotFound() {
        Long achievementId = 1L;

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            achievementServiceImpl.getAchievementById(achievementId);
        });

        assertEquals("Achievement not found", exception.getMessage());
    }

    @Test
    void updateAchievementSuccess() {
        Long achievementId = 1L;
        Employee employee = new Employee();
        employee.setId(2L);

        Achievement existingAchievement = new Achievement();
        existingAchievement.setEmployee(new Employee());

        Achievement updatedAchievement = new Achievement();
        updatedAchievement.setType((byte) 1);
        updatedAchievement.setReason("Example reason");
        updatedAchievement.setEmployee(employee);

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(existingAchievement));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(achievementRepository.save(existingAchievement)).thenReturn(existingAchievement);

        Achievement resultAchievement = achievementServiceImpl.updateAchievement(achievementId, updatedAchievement);

        assertEquals((byte) 1, existingAchievement.getType());
        assertEquals("Example reason", existingAchievement.getReason());
        assertEquals(employee, existingAchievement.getEmployee());
    }

    @Test
    void updateAchievementFail() {
        Long achievementId = 1L;
        Achievement updatedAchievement = new Achievement();

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            achievementServiceImpl.updateAchievement(achievementId, updatedAchievement);
        });

        assertEquals("Achievement not found", exception.getMessage());
    }

    @Test
    void deleteAchievementSuccess() {
        Long achievementId = 1L;

        when(achievementRepository.existsById(achievementId)).thenReturn(true);

        achievementServiceImpl.deleteAchievement(achievementId);

        verify(achievementRepository).deleteById(achievementId);
    }

    @Test
    void deleteAchievementFail() {
        Long achievementId = 1L;

        when(achievementRepository.existsById(achievementId)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            achievementServiceImpl.deleteAchievement(achievementId);
        });

        assertEquals("Achievement not found", exception.getMessage());
    }

    @Test
    void getEmployeeAchievementsSum() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
        when(achievementRepository.countByEmployeeAndType(employee1, (byte) 1)).thenReturn(10L);
        when(achievementRepository.countByEmployeeAndType(employee1, (byte) 0)).thenReturn(3L);
        when(achievementRepository.countByEmployeeAndType(employee2, (byte) 1)).thenReturn(0L);
        when(achievementRepository.countByEmployeeAndType(employee2, (byte) 0)).thenReturn(4L);

        List<EmployeeAchievementsSumDTO> employeeAchievementsSum = achievementServiceImpl.getEmployeeAchievementsSum();

        assertEquals(2, employeeAchievementsSum.size());

        assertEquals(10, employeeAchievementsSum.get(0).getTotalAchievements());
        assertEquals(3, employeeAchievementsSum.get(0).getTotalDisciplines());
        // rewardPoints = achievements - disciplines
        assertEquals(7, employeeAchievementsSum.get(0).getRewardPoints());

        assertEquals(0, employeeAchievementsSum.get(1).getTotalAchievements());
        assertEquals(4, employeeAchievementsSum.get(1).getTotalDisciplines());
        // rewardPoints = achievements - disciplines
        assertEquals(-4, employeeAchievementsSum.get(1).getRewardPoints());
    }

    @Test
    void getDepartmentAchievementsSum() {
        Department department1 = new Department();
        Department department2 = new Department();

        when(departmentRepository.findAll()).thenReturn(List.of(department1, department2));
        when(achievementRepository.countByEmployeeDepartmentAndType(department1, (byte) 1)).thenReturn(20L);
        when(achievementRepository.countByEmployeeDepartmentAndType(department1, (byte) 0)).thenReturn(5L);
        when(achievementRepository.countByEmployeeDepartmentAndType(department2, (byte) 1)).thenReturn(10L);
        when(achievementRepository.countByEmployeeDepartmentAndType(department2, (byte) 0)).thenReturn(0L);

        List<DepartmentAchievementsSumDTO> departmentAchievementsSum = achievementServiceImpl.getDepartmentAchievementsSum();

        assertEquals(2, departmentAchievementsSum.size());

        assertEquals(20, departmentAchievementsSum.get(0).getTotalAchievements());
        assertEquals(5, departmentAchievementsSum.get(0).getTotalDisciplines());
        assertEquals(15, departmentAchievementsSum.get(0).getRewardPoints());

        assertEquals(10, departmentAchievementsSum.get(1).getTotalAchievements());
        assertEquals(0, departmentAchievementsSum.get(1).getTotalDisciplines());
        assertEquals(10, departmentAchievementsSum.get(1).getRewardPoints());
    }
}