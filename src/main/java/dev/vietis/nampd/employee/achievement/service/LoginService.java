package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.entity.Employee;

public interface LoginService {
    Employee login(String email, String password);
}
