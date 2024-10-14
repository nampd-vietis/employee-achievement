package dev.vietis.nampd.employee.achievement.service.impl;

import dev.vietis.nampd.employee.achievement.mapper.EmployeeMapper;
import dev.vietis.nampd.employee.achievement.model.dto.AccountDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Department;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.repository.DepartmentRepository;
import dev.vietis.nampd.employee.achievement.repository.EmployeeRepository;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import dev.vietis.nampd.employee.achievement.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final FileStorageService fileStorageService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DepartmentRepository departmentRepository, FileStorageService fileStorageService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void createEmployee(EmployeeDTO employeeDto) {
        Employee employee = employeeMapper.toEmployee(employeeDto);

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + employee.getEmail());
        }

        Department department = departmentRepository.findByDepartmentName(employeeDto.getDepartmentName())
                .orElseThrow(() -> new NoSuchElementException("Department not found"));

        employee.setDepartment(department);
        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toEmployeeDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public EmployeeDTO getEmployeeByEmail(String email) {
//        Employee employee = employeeRepository.findByEmail(email)
//                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
//        return employeeMapper.toEmployeeDto(employee);
//    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        return employeeMapper.toEmployeeDto(employee);
    }

    @Override
    public Optional<Employee> findById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        return Optional.ofNullable(employee);
    }

    @Override
    public void updateEmployee(Long id, EmployeeDTO updatedEmployeeDTO, MultipartFile imgFile) {
        Employee existingEmployee = employeeRepository.findById(updatedEmployeeDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        existingEmployee.setFullName(updatedEmployeeDTO.getFullName());
        existingEmployee.setGender(Employee.Gender.valueOf(updatedEmployeeDTO.getGender()));

        // Kiểm tra và xử lý ảnh mới
        if (imgFile != null && !imgFile.isEmpty() && !Objects.requireNonNull(imgFile.getOriginalFilename()).isEmpty()) {
            String newImgFilename = fileStorageService.save(imgFile);  // Lưu file mới
            updatedEmployeeDTO.setPhoto(newImgFilename);               // Cập nhật đường dẫn file mới
            existingEmployee.setPhoto(newImgFilename);                 // Cập nhật ảnh cho nhân viên
        }

        existingEmployee.setBirthday(updatedEmployeeDTO.getBirthday());
        existingEmployee.setSalary(updatedEmployeeDTO.getSalary());
        existingEmployee.setLevel(updatedEmployeeDTO.getLevel());
        existingEmployee.setEmail(updatedEmployeeDTO.getEmail());
        existingEmployee.setPhoneNumber(updatedEmployeeDTO.getPhoneNumber());
        existingEmployee.setNotes(updatedEmployeeDTO.getNotes());
        existingEmployee.setPassword(updatedEmployeeDTO.getPassword());
        existingEmployee.setRole(Employee.Role.valueOf(updatedEmployeeDTO.getRole()));

        Department department = departmentRepository.findByDepartmentName(updatedEmployeeDTO.getDepartmentName())
                .orElseThrow(() -> new NoSuchElementException("Department not found"));

        existingEmployee.setDepartment(department);

        employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new NoSuchElementException("Employee not found");
        }
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public List<EmployeeDTO> searchEmployees(String fullName, String email, String phoneNumber, String departmentName) {
        List<Employee> employees = employeeRepository.searchEmployees(fullName, email, phoneNumber, departmentName);
        return employees.stream()
                .map(employeeMapper::toEmployeeDto)
                .collect(Collectors.toList());
    }

    //QUẢN LÝ TÀI KHOẢN

    // Lấy danh sách nhân viên chưa có tài khoản (password == null)
    @Override
    public List<Employee> getEmployeesWithoutAccount() {
        return employeeRepository.findByPasswordIsNull();
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        // Lấy tất cả các nhân viên từ cơ sở dữ liệu
        List<Employee> employees = employeeRepository.findAll();

        // Chuyển đổi từ Employee sang AccountDTO
        return employees.stream()
                .filter(employee -> employee.getPassword() != null)
                .map(employee -> {
                    AccountDTO accountDTO = new AccountDTO();
                    accountDTO.setEmployeeId(employee.getId());
                    accountDTO.setFullName(employee.getFullName());
                    accountDTO.setEmail(employee.getEmail());
                    accountDTO.setPassword(employee.getPassword());
                    return accountDTO;
                })
                .collect(Collectors.toList());
    }

    // Tạo tài khoản cho một nhân viên
    @Override
    public void createAccount(Long employeeId, AccountDTO accountDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeId));

        // Gán email và mã hóa mật khẩu vào Employee
        employee.setEmail(accountDTO.getEmail());
        employee.setPassword((accountDTO.getPassword()));

        employeeRepository.save(employee);
    }

    // Cập nhật tài khoản
    @Override
    public void updateAccount(Long employeeId, AccountDTO accountDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeId));

        employee.setEmail(accountDTO.getEmail());
        employee.setPassword((accountDTO.getPassword()));

        employeeRepository.save(employee);
    }

    // Xóa tài khoản của một nhân viên
    @Override
    public void deleteAccount(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeId));

        // Xóa password
        employee.setPassword(null);

        employeeRepository.save(employee);
    }
}
