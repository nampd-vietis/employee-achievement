package dev.vietis.nampd.employee.achievement.repository;

import dev.vietis.nampd.employee.achievement.model.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByEmployeeId(Long employeeId);
}
