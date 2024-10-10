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
        String email = "test@gmail.com";
        String password = "pass";
        Employee mockEmployee = new Employee();
        mockEmployee.setEmail(email);
        mockEmployee.setPassword(password);

        // Giả lập EmployeeRepository trả về đối tượng Optional chứa Employee
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(mockEmployee));

        // Gọi phương thức login
        Employee resultEmployee = loginService.login(email, password);

        // Kiểm tra kết quả trả về
        assertNotNull(resultEmployee);
        assertEquals(email, resultEmployee.getEmail());
        assertEquals(password, resultEmployee.getPassword());

        // Kiểm tra findByEmail đã được gọi đúng với tham số email
        verify(employeeRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoginWithInvalidEmail() {
        // Giả lập một email không tồn tại
        String email = "wrong@gmail.com";
        String password = "pass";

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
        String email = "test@gmail.com";
        String rightPass = "right_pass";
        String wrongPass = "wrong_pass";
        Employee mockEmployee = new Employee();
        mockEmployee.setEmail(email);
        mockEmployee.setPassword(rightPass);

        // Giả lập EmployeeRepository trả về đối tượng Optional chứa Employee
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(mockEmployee));

        // Kiểm tra xem RuntimeException được ném ra khi mật khẩu sai
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loginService.login(email, wrongPass);
        });

        // Kiểm tra thông báo lỗi
        assertEquals("Thông tin đăng nhập không hợp lệ", exception.getMessage());

        // Kiểm tra findByEmail đã được gọi đúng với tham số email
        verify(employeeRepository, times(1)).findByEmail(email);
    }
}