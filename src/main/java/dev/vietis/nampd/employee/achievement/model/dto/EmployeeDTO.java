package dev.vietis.nampd.employee.achievement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String departmentName;

    public String getGenderDisplayName() {
        switch (gender.toUpperCase()) {
            case "MALE":
                return "Nam";
            case "FEMALE":
                return "Nữ";
            case "OTHER":
                return "Khác";
            default:
                return "Không xác định"; // Nếu không có giá trị phù hợp
        }
    }
}

