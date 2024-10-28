package dev.vietis.nampd.employee.achievement.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    LOGIN_INVALID(1001, "Invalid login information"),
    EMAIL_ALREADY_EXISTS(2001, "Email already exists"),
    EMPLOYEE_NOT_FOUND(2002, "Employee not found"),
    DEPARTMENT_NOT_FOUND(3001, "Department not found"),
    DEPARTMENT_ALREADY_EXISTS(3002, "Department already exists"),
    ACHIEVEMENT_NOT_FOUND(4001, "Achievement not found")
    ;

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
