package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.mapper.EmployeeMapper;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import dev.vietis.nampd.employee.achievement.service.FileStorageService;
import org.hibernate.validator.constraints.Mod10Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEmployeeSuccess() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail("test@gmail.com");
        employeeDTO.setDepartmentName("IT");

        Employee employee = new Employee();
        employee.setEmail("test@gmail.com");

        when(employeeMapper.toEmployee(employeeDTO)).thenReturn(employee);
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(false);

        // Mô phỏng tìm kiếm phòng ban
        Department department = new Department();
        department.setDepartmentName("IT");
        when(departmentRepository.findByDepartmentName("IT")).thenReturn(Optional.of(department));

        // Gọi phương thức createEmployee của service
        employeeServiceImpl.createEmployee(employeeDTO);

        // ktra xem phương thức save có được gọi
        verify(employeeRepository).save(employee);
        //ktra phòng ban có được gán đúng hay không
        assertEquals(department, employee.getDepartment());
    }

    //exist email
    @Test
    void createEmployeeFail() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail("test@gmail.com");
        employeeDTO.setDepartmentName("IT");

        Employee employee = new Employee();
        employee.setEmail("test@gmail.com");

        when(employeeMapper.toEmployee(employeeDTO)).thenReturn(employee);
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(true);

        //ktra RuntimeExep có đc throw
        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeServiceImpl.createEmployee(employeeDTO);
        });

        assertEquals("Email đã tồn tại: test@gmail.com", exception.getMessage());
        //đảm bảo save ko được gọi
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getAllEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        Employee employee3 = new Employee();

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2, employee3));

        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        EmployeeDTO employeeDTO3 = new EmployeeDTO();

        when(employeeMapper.toEmployeeDto(employee1)).thenReturn(employeeDTO1);
        when(employeeMapper.toEmployeeDto(employee2)).thenReturn(employeeDTO2);
        when(employeeMapper.toEmployeeDto(employee3)).thenReturn(employeeDTO3);

        List<EmployeeDTO> employees = employeeServiceImpl.getAllEmployees();

        assertEquals(3, employees.size());
        assertEquals(employeeDTO1, employees.get(0));
        assertEquals(employeeDTO2, employees.get(1));
        assertEquals(employeeDTO3, employees.get(2));
    }

    @Test
    void getEmployeeByIdSuccess() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(id);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeMapper.toEmployeeDto(employee)).thenReturn(employeeDTO);

        EmployeeDTO resultEmployeeDTO = employeeServiceImpl.getEmployeeById(id);

        assertEquals(employeeDTO, resultEmployeeDTO);
    }

    //not found with id
    @Test
    void getEmployeeByIdFail() {
        Long id = 1L;

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        //ktra NoSuchElementException có đc throw
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            employeeServiceImpl.getEmployeeById(id);
        });

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void testUpdateEmployeeSuccess() {
        // Dữ liệu đầu vào
        Long employeeId = 1L;
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(employeeId);
        updatedEmployeeDTO.setFullName("Updated Name");
        updatedEmployeeDTO.setGender("MALE");
        updatedEmployeeDTO.setBirthday(LocalDate.of(1990, 1, 1));
        updatedEmployeeDTO.setSalary(new BigDecimal("5000.00")); // BigDecimal cho salary
        updatedEmployeeDTO.setLevel(2);
        updatedEmployeeDTO.setEmail("updated@gmail.com");
        updatedEmployeeDTO.setPhoneNumber("0123456789");
        updatedEmployeeDTO.setNotes("Updated Notes");
        updatedEmployeeDTO.setPassword("updatedPassword");
        updatedEmployeeDTO.setRole("USER");
        updatedEmployeeDTO.setDepartmentName("IT");

        MultipartFile imgFile = Mockito.mock(MultipartFile.class);

        // Mô phỏng kết quả trả về từ repository
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setFullName("Old Name");
        existingEmployee.setGender(Employee.Gender.MALE);
        existingEmployee.setBirthday(LocalDate.of(1985, 1, 1));
        existingEmployee.setSalary(new BigDecimal("3000.00")); // BigDecimal cho salary
        existingEmployee.setLevel(1);
        existingEmployee.setEmail("old@gmail.com");
        existingEmployee.setPhoneNumber("0987654321");
        existingEmployee.setNotes("Old Notes");
        existingEmployee.setPassword("oldPassword");
        existingEmployee.setRole(Employee.Role.ADMIN);
        existingEmployee.setPhoto("oldPhoto.jpg");

        Department department = new Department();
        department.setDepartmentName("IT");

        // Mô phỏng hành vi của các thành phần phụ thuộc
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(departmentRepository.findByDepartmentName("IT")).thenReturn(Optional.of(department));

        // Kiểm tra file ảnh không null và có tên
        when(imgFile.isEmpty()).thenReturn(false);
        when(imgFile.getOriginalFilename()).thenReturn("newPhoto.jpg");
        when(fileStorageService.save(imgFile)).thenReturn("newPhoto.jpg");

        // Gọi hàm updateEmployee
        employeeServiceImpl.updateEmployee(employeeId, updatedEmployeeDTO, imgFile);

        // Xác minh các thay đổi
        assertEquals("Updated Name", existingEmployee.getFullName());
        assertEquals(Employee.Gender.MALE, existingEmployee.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), existingEmployee.getBirthday());
        assertEquals(0, existingEmployee.getSalary().compareTo(new BigDecimal("5000.00"))); // So sánh BigDecimal
        assertEquals(2, existingEmployee.getLevel());
        assertEquals("updated@gmail.com", existingEmployee.getEmail());
        assertEquals("0123456789", existingEmployee.getPhoneNumber());
        assertEquals("Updated Notes", existingEmployee.getNotes());
        assertEquals("updatedPassword", existingEmployee.getPassword());
        assertEquals(Employee.Role.USER, existingEmployee.getRole());
        assertEquals(department, existingEmployee.getDepartment());
        assertEquals("newPhoto.jpg", existingEmployee.getPhoto());

        // Xác minh gọi save trên repository
        verify(employeeRepository).save(existingEmployee);
    }

    //not found employee to update
    @Test
    void updateEmployeeFail() {
        Long id = 1L;
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(id);
        updatedEmployeeDTO.setFullName("Updated Name");
        updatedEmployeeDTO.setGender("MALE");
        updatedEmployeeDTO.setBirthday(LocalDate.of(1990, 1, 1));
        updatedEmployeeDTO.setSalary(new BigDecimal("5000.00"));
        updatedEmployeeDTO.setLevel(2);
        updatedEmployeeDTO.setEmail("updated@gmail.com");
        updatedEmployeeDTO.setPhoneNumber("0123456789");
        updatedEmployeeDTO.setNotes("Updated Notes");
        updatedEmployeeDTO.setPassword("updatedPassword");
        updatedEmployeeDTO.setRole("USER");
        updatedEmployeeDTO.setDepartmentName("IT");

        MultipartFile imgFile = Mockito.mock(MultipartFile.class);

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            employeeServiceImpl.updateEmployee(id, updatedEmployeeDTO, imgFile);
        });

        assertEquals("Employee not found", exception.getMessage());

        verify(employeeRepository, never()).save(any(Employee.class));

    }

    @Test
    void deleteEmployeeSuccess() {
        Long id = 1L;

        when(employeeRepository.existsById(id)).thenReturn(true);

        employeeServiceImpl.deleteEmployee(id);

        verify(employeeRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteEmployeeFail() {
        Long id = 1L;

        when(employeeRepository.existsById(id)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            employeeServiceImpl.deleteEmployee(id);
        });

        assertEquals("Employee not found", exception.getMessage());
    }
}