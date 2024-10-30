package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.response.PagedResponse;
import dev.vietis.nampd.employee.achievement.service.DepartmentService;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import dev.vietis.nampd.employee.achievement.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("admin/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final FileStorageService fileStorageService;

    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService, FileStorageService fileStorageService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String getAllEmployees(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  Model model) {
        PagedResponse<EmployeeDTO> employeePagedResponse = employeeService.getEmployeesPaginated(page, size);
        model.addAttribute("employees", employeePagedResponse.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePagedResponse.getTotalPages());
        model.addAttribute("pageSize", size);
        return "employee/list";
    }

    //view detail
    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employeeDTO);
        return "employee/detail";
    }

    // Hiển thị form tạo mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());

        // Thêm danh sách phòng ban vào model
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        return "employee/add_form"; // Trả về view form tạo phòng ban
    }

    @PostMapping
    public String createEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                                 @RequestParam("imgFile") MultipartFile imgFile) {

        // Lưu file vào thư mục và lấy đường dẫn
        fileStorageService.save(imgFile); // Sử dụng phương thức save để lưu tệp
        String filePath = imgFile.getOriginalFilename(); // Lấy tên tệp đã lưu
        employeeDTO.setPhoto(filePath); // Gán tên tệp cho photo của nhân viên

        // Tạo nhân viên mới
        employeeService.createEmployee(employeeDTO);
        return "redirect:/admin/employees";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employeeDTO);

        // Thêm danh sách phòng ban vào model
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        return "employee/update_form";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employee") EmployeeDTO employeeDTO,
                                 @RequestParam("imgFile") MultipartFile imgFile) {
            employeeService.updateEmployee(id, employeeDTO, imgFile);
            return "redirect:/admin/employees";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/admin/employees";
    }

    @GetMapping("/search")
    public String searchEmployees(@RequestParam(required = false) String fullName,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String phoneNumber,
                                  @RequestParam(required = false) String departmentName,
                                  Model model) {
        List<EmployeeDTO> employees = employeeService.searchEmployees(fullName, email, phoneNumber, departmentName);
        model.addAttribute("employees", employees);
        return "employee/search_results :: employeeData"; // Trả về fragment HTML
    }
}
