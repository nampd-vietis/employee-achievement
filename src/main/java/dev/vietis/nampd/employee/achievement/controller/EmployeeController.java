package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllEmployees(Model model) {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee/list";
    }

    @GetMapping("/{email}")
    public String getEmployeeByEmail(@PathVariable String email, Model model) {
        EmployeeDTO employee = employeeService.getEmployeeByEmail(email);
        model.addAttribute("employee", employee);
        return "employee/detail";
    }

    @PostMapping
    public String createEmployee(@ModelAttribute EmployeeDTO employeeDto) {
        employeeService.createEmployee(employeeDto);
        return "redirect:/employees";
    }

    @PostMapping("/{id}/update")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute EmployeeDTO employeeDto) {
        employeeService.updateEmployee(id, employeeDto);
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}
