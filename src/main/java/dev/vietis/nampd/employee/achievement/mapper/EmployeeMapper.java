package dev.vietis.nampd.employee.achievement.mapper;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {

    //map Employee -> dto
    public EmployeeDTO toEmployeeDto(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getFullName(),
                employee.getGender().toString(),
                employee.getPhoto(),
                employee.getBirthday(),
                employee.getSalary(),
                employee.getLevel(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getNotes(),
                employee.getPassword(),
                employee.getRole().toString(),
                employee.getDepartment().getDepartmentName()
        );
    }

    public Employee toEmployee(EmployeeDTO employeeDto) {
        if (employeeDto == null) {
            return null;
        }

        Employee employee = new Employee();

        employee.setFullName(employeeDto.getFullName());
        employee.setGender(Employee.Gender.valueOf(employeeDto.getGender()));
        employee.setPhoto(employeeDto.getPhoto());
        employee.setBirthday(employeeDto.getBirthday());
        employee.setSalary(employeeDto.getSalary());
        employee.setLevel(employeeDto.getLevel());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setNotes(employeeDto.getNotes());
        employee.setPassword(employeeDto.getPassword());
        employee.setRole(Employee.Role.valueOf(employeeDto.getRole()));

        return employee;
    }

}
