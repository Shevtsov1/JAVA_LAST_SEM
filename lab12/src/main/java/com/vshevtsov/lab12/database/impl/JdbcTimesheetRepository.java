package com.vshevtsov.lab12.database.impl;

import com.vshevtsov.lab12.database.TimesheetRepository;
import com.vshevtsov.lab12.models.Timesheet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcTimesheetRepository implements TimesheetRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTimesheetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createTableIfNotExists(); // Call the method to create the table if it doesn't exist
    }

    // Method to create the "timesheets" table if it doesn't exist
    private void createTableIfNotExists() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS timesheets (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "employee_id VARCHAR(36) NOT NULL," +
                "date DATE NOT NULL," +
                "hours_worked DOUBLE NOT NULL" +
                ")");
    }

    @Override
    public double getTotalHoursWorkedForEmployee(String employeeId) {
        String query = "SELECT SUM(hours_worked) AS total_hours FROM timesheets WHERE employee_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{employeeId}, Double.class);
    }

    public void save(Timesheet timesheet) {
        String query = "INSERT INTO timesheets (employee_id, date, hours_worked) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, timesheet.getEmployeeId(), timesheet.getDate(), timesheet.getHoursWorked());
    }

    @Override
    public void update(Timesheet timesheet) {
        String query = "UPDATE timesheets SET employee_id = ?, date = ?, hours_worked = ? WHERE id = ?";
        jdbcTemplate.update(query, timesheet.getEmployeeId(), timesheet.getDate(), timesheet.getHoursWorked(), timesheet.getId());
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM timesheets WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public void deleteByEmployeeId(String id) {
        String query = "DELETE FROM timesheets WHERE employee_id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public Timesheet findById(Long id) {
        String query = "SELECT * FROM timesheets WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (resultSet, rowNum) -> mapTimesheet(resultSet));
    }

    @Override
    public Timesheet findByEmployeeId(String id) {
        String query = "SELECT * FROM timesheets WHERE employee_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (resultSet, rowNum) -> mapTimesheet(resultSet));
    }

    @Override
    public List<Timesheet> findAll() {
        String query = "SELECT * FROM timesheets";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> mapTimesheet(resultSet));
    }

    private Timesheet mapTimesheet(ResultSet resultSet) throws SQLException {
        Timesheet timesheet = new Timesheet();
        timesheet.setId(resultSet.getLong("id"));
        timesheet.setEmployeeId(resultSet.getString("employee_id"));
        timesheet.setDate(resultSet.getDate("date").toLocalDate());
        timesheet.setHoursWorked(resultSet.getDouble("hours_worked"));
        return timesheet;
    }
}
