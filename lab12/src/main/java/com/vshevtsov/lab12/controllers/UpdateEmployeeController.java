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
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class UpdateEmployeeController {

    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    public UpdateEmployeeController(EmployeeRepository employeeRepository, TimesheetRepository timesheetRepository) {
        this.employeeRepository = employeeRepository;
        this.timesheetRepository = timesheetRepository;
    }

    @PostMapping("/edit")
    public String updateEmployee(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("patronymic") String patronymic,
            @RequestParam("position") String position,
            @RequestParam("hourlyRate") double hourlyRate,
            @RequestParam(value = "hoursWorked", required = false) Integer hoursWorked) {

        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeRepository.findById(id));
        Optional<Timesheet> optionalTimesheet = Optional.ofNullable(timesheetRepository.findByEmployeeId(id));
        if (optionalEmployee.isPresent() && optionalTimesheet.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setName(name);
            employee.setSurname(surname);
            employee.setPatronymic(patronymic);
            employee.setPosition(position);
            employee.setHourlyRate(hourlyRate);
            Timesheet timesheet = optionalTimesheet.get();
            timesheet.setDate(LocalDate.now());
            timesheet.setHoursWorked(hoursWorked);

            employeeRepository.update(employee);
            timesheetRepository.update(timesheet);
        }
        return "redirect:/employees";
    }

    @PostMapping("/delete")
    public String DeleteEmployee(
            @RequestParam("id") String id) {

        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeRepository.findById(id));
        Optional<Timesheet> optionalTimesheet = Optional.ofNullable(timesheetRepository.findByEmployeeId(id));
        if (optionalEmployee.isPresent() && optionalTimesheet.isPresent()) {
            employeeRepository.delete(optionalEmployee.get().getId());
            timesheetRepository.deleteByEmployeeId(optionalTimesheet.get().getEmployeeId());
        }
        return "redirect:/employees";
    }
}
