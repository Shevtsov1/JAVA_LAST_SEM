package com.vshevtsov.lab12.database.impl;

import com.vshevtsov.lab12.database.EmployeeRepository;
import com.vshevtsov.lab12.models.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcEmployeeRepository implements EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createTableIfNotExists(); // Call the method to create the table if it doesn't exist
    }

    // Method to create the "employees" table if it doesn't exist
    private void createTableIfNotExists() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS employees (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "surname VARCHAR(255) NOT NULL," +
                "patronymic VARCHAR(255) NOT NULL," +
                "position VARCHAR(255) NOT NULL," +
                "salary DOUBLE NOT NULL" +
                ")");
    }

    public Employee getHourlyRateAndNameById(String id) {
        String query = "SELECT id, name, surname, patronymic, salary FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (resultSet, rowNum) -> {
            Employee employee = new Employee();
            employee.setId(resultSet.getString("id"));
            employee.setName(resultSet.getString("name"));
            employee.setSurname(resultSet.getString("surname"));
            employee.setPatronymic(resultSet.getString("patronymic"));
            employee.setHourlyRate(resultSet.getDouble("salary"));
            return employee;
        });
    }

    public @ResponseBody String save(Employee employee) {
        String checkQuery = "SELECT COUNT(*) FROM employees WHERE name = ? AND surname = ? AND patronymic = ? AND position = ?";
        int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, employee.getName(), employee.getSurname(), employee.getPatronymic(), employee.getPosition());

        if (count > 0) {
            return "Пользователь с такими ФИО и должностью уже существует";
        }

        String insertQuery = "INSERT INTO employees (id, name, surname, patronymic, position, salary) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery, employee.getId(), employee.getName(), employee.getSurname(), employee.getPatronymic(), employee.getPosition(), employee.getHourlyRate());

        return "Пользователь успешно добавлен";
    }

    public void update(Employee employee) {
        String query = "UPDATE employees SET name = ?, surname = ?, patronymic = ?, position = ?, salary = ? WHERE id = ?";
        jdbcTemplate.update(query, employee.getName(), employee.getSurname(), employee.getPatronymic(), employee.getPosition(), employee.getHourlyRate(), employee.getId());
    }

    public Employee findById(String id) {
        String query = "SELECT * FROM employees WHERE id = ?";
        List<Employee> employees = jdbcTemplate.query(query, (resultSet, rowNum) -> mapEmployee(resultSet), id);

        return employees.isEmpty() ? null : employees.get(0);
    }

    public List<Employee> findAll() {
        String query = "SELECT * FROM employees";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> mapEmployee(resultSet));
    }

    private Employee mapEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getString("id"));
        employee.setName(resultSet.getString("name"));
        employee.setSurname(resultSet.getString("surname"));
        employee.setPatronymic(resultSet.getString("patronymic"));
        employee.setPosition(resultSet.getString("position"));
        employee.setHourlyRate(resultSet.getDouble("salary"));
        return employee;
    }

    @Override
    public void delete(String id) {
        String query = "DELETE FROM employees WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

}
