package dev.vietis.nampd.employee.achievement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Long id;
    private String departmentName;
    private Set<EmployeeDTO> employeeDTOSet;
}
