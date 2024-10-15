package dev.vietis.nampd.employee.achievement.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private Long employeeId;

    private String fullName;

    @Email(message = "Invalid email")
    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;
}
