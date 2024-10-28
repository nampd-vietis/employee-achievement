package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.DepartmentDTO;
import dev.vietis.nampd.employee.achievement.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Lấy tất cả phòng ban
    @GetMapping
    public String getAllDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "department/list";
    }

//    // Lấy phòng ban theo ID
//    @GetMapping("/{id}")
//    public String getDepartmentById(@PathVariable Long id, Model model) {
//        try {
//            DepartmentDTO department = departmentService.getDepartmentById(id);
//            model.addAttribute("department", department);
//            return "addForm"; // Trả về view chi tiết phòng ban
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("error", "Department not exist");
//            return "error"; // Trả về view lỗi
//        }
//    }

    // Hiển thị form tạo mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new DepartmentDTO());
        return "department/add_form";
    }

    // Xử lý tạo mới
    @PostMapping
    public String createDepartment(@ModelAttribute("department") DepartmentDTO departmentDTO) {
        departmentService.createDepartment(departmentDTO);
        return "redirect:/admin/departments";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        return "department/update_form";
    }

    // Xử lý chỉnh sửa
    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable Long id,
                                   @ModelAttribute("department") DepartmentDTO departmentDTO) {
        departmentService.updateDepartment(id, departmentDTO);
        return "redirect:/admin/departments";
    }

    // Xóa phòng ban
    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, Model model) {
        departmentService.deleteDepartment(id);
        return "redirect:/admin/departments";
    }

//    // Xem danh sách nhân viên thuộc phòng ban
//    @GetMapping("/{id}/employees")
//    public String getEmployeesInDepartment(@PathVariable Long id, Model model) {
//        try {
//            List<Employee> employees = departmentService.getAllEmployeesInDepartment(id);
//            model.addAttribute("employees", employees);
//            return "employee/list"; // Trả về view danh sách nhân viên trong phòng ban
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("error", "Phòng ban không tồn tại.");
//            return "error"; // Trả về view lỗi
//        }
//    }
}
