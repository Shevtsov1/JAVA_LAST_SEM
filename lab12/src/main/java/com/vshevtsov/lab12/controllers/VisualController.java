package com.vshevtsov.lab12.controllers;

import com.vshevtsov.lab12.database.EmployeeRepository;
import com.vshevtsov.lab12.database.TimesheetRepository;
import com.vshevtsov.lab12.models.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VisualController {

    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    public VisualController(EmployeeRepository employeeRepository, TimesheetRepository timesheetRepository) {
        this.employeeRepository = employeeRepository;
        this.timesheetRepository = timesheetRepository;
    }

    @GetMapping("/home")
    public String displayHome() {
        return "home";
    }

    @GetMapping("/employees")
    public String displayAllEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("timesheets", timesheetRepository.findAll());
        return "employees";
    }

    @GetMapping("/employees/add")
    public String displayAddEmployeeForm() {
        return "add_employee";
    }

    @GetMapping("/employees/edit")
    public String displayEditEmployeesForm(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("timesheets", timesheetRepository.findAll());
        return "edit_employee";
    }

}
