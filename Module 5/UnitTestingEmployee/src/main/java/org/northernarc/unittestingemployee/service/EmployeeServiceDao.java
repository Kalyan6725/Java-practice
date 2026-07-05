package org.northernarc.unittestingemployee.service;

import org.northernarc.unittestingemployee.model.Employee;

import java.util.List;

public interface EmployeeServiceDao {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employeeDetails);
    void deleteEmployee(Long id);

}
