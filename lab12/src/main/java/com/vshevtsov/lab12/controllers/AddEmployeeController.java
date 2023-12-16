package com.vshevtsov.lab12.controllers;

import com.vshevtsov.lab12.database.EmployeeRepository;
import com.vshevtsov.lab12.database.TimesheetRepository;
import com.vshevtsov.lab12.models.Employee;
import com.vshevtsov.lab12.models.Timesheet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/employees")
public class AddEmployeeController {

    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    public AddEmployeeController(EmployeeRepository employeeRepository, TimesheetRepository timesheetRepository) {
        this.employeeRepository = employeeRepository;
        this.timesheetRepository = timesheetRepository;
    }

    @GetMapping("/employees/add")
    public String showAddEmployeeForm() {
        return "add-employee"; // Возвращает имя вашего HTML-файла с формой
    }

    @PostMapping
    public String addEmployee(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("patronymic") String patronymic,
            @RequestParam("position") String position,
            @RequestParam("hourlyRate") double hourlyRate,
            @RequestParam(value = "hoursWorked", required = false) Integer hoursWorked
    ) {
        // Создайте новый объект Employee с полученными данными
        Employee employee = new Employee();
        employee.setName(name);
        employee.setSurname(surname);
        employee.setPatronymic(patronymic);
        employee.setPosition(position);
        employee.setHourlyRate(hourlyRate);

        if (hoursWorked != null) {
            Timesheet timesheet = new Timesheet();
            timesheet.setEmployeeId(employee.getId());
            timesheet.setDate(LocalDate.now());
            timesheet.setHoursWorked(hoursWorked);
            timesheetRepository.save(timesheet);
        } else {
            Timesheet timesheet = new Timesheet();
            timesheet.setEmployeeId(employee.getId());
            timesheet.setDate(LocalDate.now());
            timesheet.setHoursWorked(0);
            timesheetRepository.save(timesheet);
        }

        // Сохраните нового сотрудника в базе данных
        employeeRepository.save(employee);

        // Перенаправьте пользователя на страницу со списком сотрудников или на другую страницу, если это необходимо
        return "redirect:/employees"; // Измените путь, если нужно
    }
}
