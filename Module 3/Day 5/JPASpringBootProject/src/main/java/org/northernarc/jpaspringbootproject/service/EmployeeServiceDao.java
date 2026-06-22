package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.model.Employee;
import org.northernarc.jpaspringbootproject.model.Project;

import java.util.List;

public interface EmployeeServiceDao {
    public Employee addEmployee(Employee employee);
    public List<Employee> getAll();
    public Employee getById(Long id);
    public void deleteById(Long id);
    public void assignProject(Long pid, Long eid);
    public List<Project> getProjectsByEmployeeId(int id);
}
