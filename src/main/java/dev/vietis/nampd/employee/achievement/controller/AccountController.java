package dev.vietis.nampd.employee.achievement.controller;

import dev.vietis.nampd.employee.achievement.model.dto.AccountDTO;
import dev.vietis.nampd.employee.achievement.model.dto.EmployeeDTO;
import dev.vietis.nampd.employee.achievement.model.entity.Employee;
import dev.vietis.nampd.employee.achievement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/accounts")
public class AccountController {

    private final EmployeeService employeeService;

    public AccountController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllAccounts(Model model) {
        List<AccountDTO> accounts = employeeService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "account/list";
    }

    // Hiển thị form tạo tài khoản
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        // Lấy danh sách các nhân viên chưa có tài khoản
        List<Employee> employeesWithoutAccount = employeeService.getEmployeesWithoutAccount();
        System.out.println(employeesWithoutAccount);

        model.addAttribute("account", new AccountDTO());
        model.addAttribute("employeesWithoutAccount", employeesWithoutAccount);
        return "account/addForm";
    }

    // Xử lý việc tạo tài khoản
    @PostMapping("/{employeeId}")
    public String createAccount(@PathVariable Long employeeId,
                                @ModelAttribute @Valid AccountDTO accountDTO,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "account/addForm";
        }
        employeeService.createAccount(employeeId, accountDTO);
        return "redirect:/admin/accounts";
    }

    // Hiển thị form để chỉnh sửa tài khoản
    @GetMapping("/edit/{employeeId}")
    public String editAccountForm(@PathVariable Long employeeId, Model model) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeId));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(employee.getEmail());
        model.addAttribute("account", accountDTO);
        return "account/updateForm";
    }

    // Xử lý việc cập nhật tài khoản
    @PostMapping("/update/{employeeId}")
    public String updateAccount(@PathVariable Long employeeId,
                                @ModelAttribute @Valid AccountDTO accountDTO,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "account/updateForm";
        }

        employeeService.updateAccount(employeeId, accountDTO);
        return "redirect:/admin/accounts";
    }

    // Xóa tài khoản
    @GetMapping("/delete/{employeeId}")
    public String deleteAccount(@PathVariable Long employeeId) {
        employeeService.deleteAccount(employeeId);
        return "redirect:/admin/accounts";
    }
}