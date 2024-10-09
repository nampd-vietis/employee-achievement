package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        // Giả lập dữ liệu
        String email = "employee@example.com";
        String password = "password";
        Employee mockEmployee = new Employee();
        mockEmployee.setEmail(email);
        mockEmployee.setPassword(password);

        // Giả lập EmployeeRepository trả về đối tượng Optional chứa Employee
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(mockEmployee));

        // Gọi phương thức login
        Employee result = loginService.login(email, password);

        // Kiểm tra kết quả trả về
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());

        // Kiểm tra findByEmail đã được gọi đúng với tham số email
        verify(employeeRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoginWithInvalidEmail() {
        // Giả lập một email không tồn tại
        String email = "nonexistent@example.com";
        String password = "any_password";

        // Giả lập EmployeeRepository trả về Optional trống
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Kiểm tra xem RuntimeException được ném ra
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loginService.login(email, password);
        });

        // Kiểm tra thông báo lỗi
        assertEquals("Thông tin đăng nhập không hợp lệ", exception.getMessage());

        // Kiểm tra findByEmail đã được gọi đúng với tham số email
        verify(employeeRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoginWithIncorrectPassword() {
        // Giả lập dữ liệu nhân viên
        String email = "employee@example.com";
        String correctPassword = "correct_password";
        String incorrectPassword = "incorrect_password";
        Employee mockEmployee = new Employee();
        mockEmployee.setEmail(email);
        mockEmployee.setPassword(correctPassword);

        // Giả lập EmployeeRepository trả về đối tượng Optional chứa Employee
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(mockEmployee));

        // Kiểm tra xem RuntimeException được ném ra khi mật khẩu sai
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loginService.login(email, incorrectPassword);
        });

        // Kiểm tra thông báo lỗi
        assertEquals("Thông tin đăng nhập không hợp lệ", exception.getMessage());

        // Kiểm tra findByEmail đã được gọi đúng với tham số email
        verify(employeeRepository, times(1)).findByEmail(email);
    }
}