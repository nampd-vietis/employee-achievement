package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(EmployeeDTO employeeDto);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeByEmail(String email);
    EmployeeDTO updateEmployee(EmployeeDTO updatedEmployeeDTO, MultipartFile imgFile);
    void deleteEmployee(Long id);

    EmployeeDTO getEmployeeById(Long id);
}
