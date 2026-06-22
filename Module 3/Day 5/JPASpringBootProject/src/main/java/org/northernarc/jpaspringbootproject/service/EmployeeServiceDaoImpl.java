package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.model.Employee;
import org.northernarc.jpaspringbootproject.model.Project;
import org.northernarc.jpaspringbootproject.repository.EmployeeRepository;
import org.northernarc.jpaspringbootproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeServiceDaoImpl implements EmployeeServiceDao {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(Math.toIntExact(id));
    }
    @Override
    public void assignProject(Long pid, Long eid) {
        Project existing = projectRepository.findById(Math.toIntExact(pid)).orElse(null);
        if(existing != null){
            Employee employee = employeeRepository.findById(Math.toIntExact(eid)).orElse(null);
            if(employee != null){
                existing.getEmployees().add(employee);
                employee.getProjects().add(existing);
                projectRepository.save(existing);
                employeeRepository.save(employee);
            }
        }
    }
    @Override
    public List<Project> getProjectsByEmployeeId(int id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        return employee.getProjects();
    }
}
