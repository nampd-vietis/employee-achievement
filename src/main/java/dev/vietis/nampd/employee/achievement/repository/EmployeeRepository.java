package dev.vietis.nampd.employee.achievement.repository;

import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Employee> findByPasswordIsNull();

    @Query("SELECT e FROM Employee e LEFT JOIN e.department d WHERE " +
            "(e.fullName LIKE %:fullName% OR :fullName IS NULL) AND " +
            "(e.email LIKE %:email% OR :email IS NULL) AND " +
            "(e.phoneNumber LIKE %:phoneNumber% OR :phoneNumber IS NULL) AND " +
            "(d.departmentName LIKE %:departmentName% OR :departmentName IS NULL)")
    List<Employee> searchEmployees(@Param("fullName") String fullName,
                                   @Param("email") String email,
                                   @Param("phoneNumber") String phoneNumber,
                                   @Param("departmentName") String departmentName);
}
