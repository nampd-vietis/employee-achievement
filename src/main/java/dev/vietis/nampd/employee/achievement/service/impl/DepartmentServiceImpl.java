package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.mapper.DepartmentMapper;
import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.service.DepartmentService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public void createDepartment(DepartmentDTO departmentDto) {
        if (departmentRepository.existsByDepartmentName(departmentDto.getDepartmentName())) {
            throw new IllegalArgumentException("Department already exists");
        }

        Department department = departmentMapper.toDepartment(departmentDto);
        Department savedDepartment = departmentRepository.save(department);

        departmentMapper.toDepartmentDto(savedDepartment);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(departmentMapper::toDepartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        return departmentMapper.toDepartmentDto(department);
    }

    @Override
    public DepartmentDTO getDepartmentByName(String name) {
        Department department = departmentRepository.findByDepartmentName(name)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        return departmentMapper.toDepartmentDto(department);
    }

    @Override
    public void updateDepartment(Long departmentId, DepartmentDTO updatedDepartmentDTO) {
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        existingDepartment.setDepartmentName(updatedDepartmentDTO.getDepartmentName());

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        departmentMapper.toDepartmentDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
        if (existingDepartment.isPresent()) {
            departmentRepository.deleteById(departmentId);
        } else {
            throw new NoSuchElementException("Department not found with id: " + departmentId);
        }
    }

    @Override
    public List<Employee> getAllEmployeesInDepartment(Long departmentId) {
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
        if (existingDepartment.isPresent()) {
            Department department = existingDepartment.get();
            return department.getEmployeeList();
        } else {
            throw new NoSuchElementException("Department not found");
        }
    }
}
