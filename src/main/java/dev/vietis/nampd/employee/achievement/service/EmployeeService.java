package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDto);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeByEmail(String email);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployee);
    void deleteEmployee(Long id);
}
