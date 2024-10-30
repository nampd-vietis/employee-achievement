package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.exception.AppException;
import dev.vietis.nampd.employee.achievement.exception.ErrorCode;
import dev.vietis.nampd.employee.achievement.mapper.DepartmentMapper;
import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.response.PagedResponse;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new AppException(ErrorCode.DEPARTMENT_ALREADY_EXISTS);
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
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return departmentMapper.toDepartmentDto(department);
    }

    @Override
    public void updateDepartment(Long departmentId, DepartmentDTO updatedDepartmentDTO) {
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        existingDepartment.setDepartmentName(updatedDepartmentDTO.getDepartmentName());

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        departmentMapper.toDepartmentDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        departmentRepository.delete(department);
    }

    @Override
    public PagedResponse<DepartmentDTO> getDepartmentsPaginated(int page, int size) {
        Page<Department> departmentPage = departmentRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id")));

        List<DepartmentDTO> departmentDtoList = departmentPage
                .map(departmentMapper::toDepartmentDto)
                .getContent();

        return new PagedResponse<>(
                departmentDtoList,
                departmentPage.getNumber(),
                departmentPage.getSize(),
                departmentPage.getTotalElements(),
                departmentPage.getTotalPages()
        );
    }

//    @Override
//    public List<Employee> getAllEmployeesInDepartment(Long departmentId) {
//        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
//        if (existingDepartment.isPresent()) {
//            Department department = existingDepartment.get();
//            return department.getEmployeeList();
//        } else {
//            throw new NoSuchElementException("Department not found");
//        }
//    }
}
