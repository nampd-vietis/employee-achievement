package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.mapper.EmployeeMapper;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import dev.vietis.nampd.employee.achievement.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final FileStorageService fileStorageService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DepartmentRepository departmentRepository, FileStorageService fileStorageService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Employee createEmployee(EmployeeDTO employeeDto) {
        Employee employee = employeeMapper.toEmployee(employeeDto);
        Department department = departmentRepository.findByDepartmentName(employeeDto.getDepartmentName())
                .orElseThrow(() -> new NoSuchElementException("Department not found"));

        employee.setDepartment(department);
        return employeeRepository.save(employee);
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
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        return employeeMapper.toEmployeeDto(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO updatedEmployeeDTO, MultipartFile imgFile) {
        Employee existingEmployee = employeeRepository.findById(updatedEmployeeDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        existingEmployee.setFullName(updatedEmployeeDTO.getFullName());
        existingEmployee.setGender(Employee.Gender.valueOf(updatedEmployeeDTO.getGenderDisplayName()));

        // Ktra xem có ảnh không
        if (updatedEmployeeDTO.getPhoto() != null && !updatedEmployeeDTO.getPhoto().isEmpty()) {
            // Nếu có, xóa
            if (existingEmployee.getPhoto() != null) {
                fileStorageService.delete(existingEmployee.getPhoto());
            }
            // Lưu ảnh mới
            fileStorageService.save(imgFile);
            String newImgFilename = imgFile.getOriginalFilename();
            existingEmployee.setPhoto(newImgFilename); // Cập nhật đường dẫn ảnh mới
        }
        existingEmployee.setBirthday(updatedEmployeeDTO.getBirthday());
        existingEmployee.setSalary(updatedEmployeeDTO.getSalary());
        existingEmployee.setLevel(updatedEmployeeDTO.getLevel());
        existingEmployee.setEmail(updatedEmployeeDTO.getEmail());
        existingEmployee.setPhoneNumber(updatedEmployeeDTO.getPhoneNumber());
        existingEmployee.setNotes(updatedEmployeeDTO.getNotes());
        existingEmployee.setPassword(updatedEmployeeDTO.getPassword());
        existingEmployee.setRole(Employee.Role.valueOf(updatedEmployeeDTO.getRole()));

        Department department = departmentRepository.findByDepartmentName(updatedEmployeeDTO.getDepartmentName())
                .orElseThrow(() -> new NoSuchElementException("Department not found"));

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
