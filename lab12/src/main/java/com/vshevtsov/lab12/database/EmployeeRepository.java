package com.vshevtsov.lab12.database;

import com.vshevtsov.lab12.models.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository {
    String save(Employee employee);

    void update(Employee employee);

    void delete(String id);

    Employee findById(String id);

    List<Employee> findAll();
}
