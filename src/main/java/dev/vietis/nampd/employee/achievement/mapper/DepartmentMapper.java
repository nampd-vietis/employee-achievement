package dev.vietis.nampd.employee.achievement.mapper;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DepartmentMapper {
    private final EmployeeMapper employeeMapper;

    public DepartmentMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    //map departmentDto -> department
    public Department toDepartment(DepartmentDTO dto) {
        var department = new Department();
        department.setDepartmentName(dto.getDepartmentName());

        return department;
    }

    //map department -> departmentDto
    public DepartmentDTO toDepartmentDto(Department department) {
        Set<EmployeeDTO> employeeDTOList = department.getEmployeeList()
                .stream()
                .map(employeeMapper::toEmployeeDto)
                .collect(Collectors.toSet());

        return new DepartmentDTO(
                department.getId(),
                department.getDepartmentName(),
                employeeDTOList
        );
    }

}
