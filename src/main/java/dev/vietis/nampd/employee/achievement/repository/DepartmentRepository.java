package dev.vietis.nampd.employee.achievement.repository;

import dev.vietis.nampd.employee.achievement.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentName(String name);
    Optional<Department> findByDepartmentName(String name);
}
