package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;

import java.util.List;

public interface DepartmentService {
    void createDepartment(DepartmentDTO departmentDto);
    List<DepartmentDTO> getAllDepartments();
    DepartmentDTO getDepartmentById(Long departmentId);
//    DepartmentDTO getDepartmentByName(String name);
    void updateDepartment(Long departmentId, DepartmentDTO updatedDepartmentDTO);
    void deleteDepartment(Long departmentId);
//    List<Employee> getAllEmployeesInDepartment(Long departmentId);
}

