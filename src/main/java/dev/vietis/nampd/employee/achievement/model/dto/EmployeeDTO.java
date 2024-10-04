package dev.vietis.nampd.employee.achievement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String fullName;
    private String gender;
    private String photo;
    private LocalDate birthday;
    private BigDecimal salary;
    private Integer level;
    private String email;
    private String phoneNumber;
    private String notes;
    private String password;
    private String role;
    private Long departmentId;

}

