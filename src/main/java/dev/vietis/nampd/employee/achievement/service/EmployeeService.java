package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.dto.AccountDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.model.response.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    void createEmployee(EmployeeDTO employeeDto);
    List<EmployeeDTO> getAllEmployees();
//    EmployeeDTO getEmployeeByEmail(String email);
    void updateEmployee(Long id, EmployeeDTO updatedEmployeeDTO, MultipartFile imgFile);
    void deleteEmployee(Long id);

    PagedResponse<EmployeeDTO> getEmployeesPaginated(int page, int size);

    EmployeeDTO getEmployeeById(Long id);
    List<EmployeeDTO> searchEmployees(String fullName, String email, String phoneNumber, String departmentName);

    PagedResponse<AccountDTO> getAccountsPaginated(int page, int size);

    //quản lý tài khoản
    // Tạo tài khoản cho một nhân viên
    void createAccount(AccountDTO accountDTO);

    // Cập nhật tài khoản
    void updateAccount(Long employeeId, AccountDTO accountDTO);

    // Xóa tài khoản của một nhân viên
    void deleteAccount(Long employeeId);

    Optional<Employee> findById(Long employeeId);

    List<Employee> getEmployeesWithoutAccount();

    List<AccountDTO> getAllAccounts();
}
