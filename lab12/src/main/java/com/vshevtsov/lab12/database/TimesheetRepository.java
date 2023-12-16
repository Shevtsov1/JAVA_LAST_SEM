package com.vshevtsov.lab12.database;

import com.vshevtsov.lab12.models.Timesheet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimesheetRepository {
    void save(Timesheet timesheet);

    void update(Timesheet timesheet);

    void delete(Long id);

    void deleteByEmployeeId(String id);

    Timesheet findById(Long id);

    Timesheet findByEmployeeId(String employeeId);

    List<Timesheet> findAll();

    double getTotalHoursWorkedForEmployee(String id);
}
