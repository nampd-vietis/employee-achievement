package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.exception.ErrorCode;
import dev.vietis.nampd.employee.achievement.exception.AppException;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import dev.vietis.nampd.employee.achievement.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private final EmployeeRepository employeeRepository;

    public LoginServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee login(String email, String password) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isEmpty() || !employee.get().getPassword().equals(password)) {
            throw new AppException(ErrorCode.LOGIN_INVALID);
        }
        return employee.get();
    }
}
