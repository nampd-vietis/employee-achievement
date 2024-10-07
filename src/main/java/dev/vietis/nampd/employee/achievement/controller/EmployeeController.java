package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.service.DepartmentService;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import dev.vietis.nampd.employee.achievement.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/employees")
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
    public String getAllEmployees(Model model) {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee/list";
    }

    //view detail
    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        try {
            EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employeeDTO);
            return "employee/detail";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Nhân viên không tồn tại");
            return "error"; // Trả về view lỗi
        }
    }

    // Hiển thị form tạo mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());

        // Thêm danh sách phòng ban vào model
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        return "employee/addForm"; // Trả về view form tạo phòng ban
    }

    // Xử lý tạo mới
    @PostMapping
    public String createEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                                 Model model,
                                 @RequestParam("imgFile") MultipartFile imgFile) {

        try {
            // Lưu file vào thư mục và lấy đường dẫn
            fileStorageService.save(imgFile); // Sử dụng phương thức save để lưu tệp
            String filePath = imgFile.getOriginalFilename(); // Lấy tên tệp đã lưu
            employeeDTO.setPhoto(filePath); // Gán tên tệp cho photo của nhân viên

            // Tạo nhân viên mới
            employeeService.createEmployee(employeeDTO);
            return "redirect:/employees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Lỗi khi tạo nhân viên");
            return "employee/addForm";
        }
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
            model.addAttribute("employee", employeeDTO);
            return "employee/updateForm";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Nhân viên không tồn tại");
            return "error";
        }
    }

    // Xử lý chỉnh sửa
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employee") EmployeeDTO employeeDTO,
                                 @RequestParam("imgFile") MultipartFile imgFile,
                                 Model model) {
        try {
            // Gọi service để cập nhật nhân viên, truyền vào photo
            employeeService.updateEmployee(employeeDTO, imgFile);
            return "redirect:/employees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Nhân viên không tồn tại");
            return "employee/updateForm"; // Trả về form cập nhật
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "employee/updateForm"; // Trả về form cập nhật nếu có lỗi
        }
    }


    // Xóa
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        try {
            employeeService.deleteEmployee(id);
            return "redirect:/employees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Nhân viên không tồn tại.");
            return "error"; // Trả về view lỗi
        }
    }
}
