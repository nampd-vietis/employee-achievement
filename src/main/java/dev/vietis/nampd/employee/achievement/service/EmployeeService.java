package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    void createEmployee(EmployeeDTO employeeDto);
    List<EmployeeDTO> getAllEmployees();
//    EmployeeDTO getEmployeeByEmail(String email);
    void updateEmployee(Long id, EmployeeDTO updatedEmployeeDTO, MultipartFile imgFile);
    void deleteEmployee(Long id);
    EmployeeDTO getEmployeeById(Long id);
}
