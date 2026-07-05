package org.northernarc.unittestingemployee.service;

import org.northernarc.unittestingemployee.exceptions.EmployeeNotFoundException;
import org.northernarc.unittestingemployee.model.Employee;
import org.northernarc.unittestingemployee.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeServiceDao {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employee.setName(employeeDetails.getName());
        employee.setSalary(employeeDetails.getSalary());
        return employeeRepo.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        employeeRepo.delete(employee);
    }
}
