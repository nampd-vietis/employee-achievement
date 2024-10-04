package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.mapper.EmployeeMapper;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDto) {
        Employee employee = employeeMapper.toEmployee(employeeDto);
        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        employee.setDepartment(department);
        return employeeMapper.toEmployeeDto(employeeRepository.save(employee));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        return employeeMapper.toEmployeeDto(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployeeDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        existingEmployee.setFullName(updatedEmployeeDTO.getFullName());
        existingEmployee.setGender(updatedEmployeeDTO.getGender());
        existingEmployee.setPhoto(updatedEmployeeDTO.getPhoto());
        existingEmployee.setBirthday(updatedEmployeeDTO.getBirthday());
        existingEmployee.setSalary(updatedEmployeeDTO.getSalary());
        existingEmployee.setLevel(updatedEmployeeDTO.getLevel());
        existingEmployee.setEmail(updatedEmployeeDTO.getEmail());
        existingEmployee.setPhoneNumber(updatedEmployeeDTO.getPhoneNumber());
        existingEmployee.setNotes(updatedEmployeeDTO.getNotes());
        existingEmployee.setPassword(updatedEmployeeDTO.getPassword());
        existingEmployee.setRole(Employee.Role.valueOf(updatedEmployeeDTO.getRole()));

        Department department = departmentRepository.findById(updatedEmployeeDTO.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        existingEmployee.setDepartment(department);

        return employeeMapper.toEmployeeDto(employeeRepository.save(existingEmployee));
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new NoSuchElementException("Employee not found");
        }
        employeeRepository.deleteById(employeeId);
    }
}
