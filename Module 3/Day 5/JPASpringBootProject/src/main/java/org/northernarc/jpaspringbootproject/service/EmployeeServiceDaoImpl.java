package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.dto.EmployeeRequestDTO;
import org.northernarc.jpaspringbootproject.dto.EmployeeResponseDTO;
import org.northernarc.jpaspringbootproject.dto.ProjectResponseDTO;
import org.northernarc.jpaspringbootproject.model.Employee;
import org.northernarc.jpaspringbootproject.model.Project;
import org.northernarc.jpaspringbootproject.repository.EmployeeRepository;
import org.northernarc.jpaspringbootproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeServiceDaoImpl implements EmployeeServiceDao {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Override
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = new Employee();
        employee.setName(employeeRequestDTO.getName());
        employee.setEmail(employeeRequestDTO.getEmail());
        if (employeeRequestDTO.getProjects() != null) {
            employee.setProjects(employeeRequestDTO.getProjects());
            for (Project project : employeeRequestDTO.getProjects()) {
                project.getEmployees().add(employee);
            }
        }
        employeeRepository.save(employee);
        return new EmployeeResponseDTO(employee.getId(), employee.getName(), employee.getProjects());
    }

    @Override
    public List<EmployeeResponseDTO> getAll() {

        List<Employee> employees = employeeRepository.findAll();
        // Convert Employee entities to EmployeeResponseDTO
        List<EmployeeResponseDTO> employeeDTOs = employees.stream()
                .map(emp -> new EmployeeResponseDTO(emp.getId(), emp.getName(), emp.getProjects()))
                .toList();
        return employeeDTOs;
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        Employee employee = employeeRepository.findById(Math.toIntExact(id)).orElse(null);
        if (employee != null) {
            return new EmployeeResponseDTO(employee.getId(), employee.getName(), employee.getProjects());
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public void assignProject(Long pid, Long eid) {
        Project existing = projectRepository.findById(Math.toIntExact(pid)).orElse(null);
        if (existing != null) {
            Employee employee = employeeRepository.findById(Math.toIntExact(eid)).orElse(null);
            if (employee != null) {
                existing.getEmployees().add(employee);
                employee.getProjects().add(existing);
                projectRepository.save(existing);
                employeeRepository.save(employee);
            }
        }
    }

    @Override
    public List<ProjectResponseDTO> getProjectsByEmployeeId(int id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        return employee.getProjects().stream()
                .map(project -> new ProjectResponseDTO(project.getId(), project.getName(), project.getEmployees()))
                .toList();
    }

    @Override
    public List<EmployeeResponseDTO> getEmployeesByPage(int page, int size) {
        List<Employee> employees = employeeRepository.findAll(PageRequest.of(page, size, Sort.by("name"))).getContent();
        return employees.stream()
                .map(emp -> new EmployeeResponseDTO(emp.getId(), emp.getName(), emp.getProjects()))
                .toList();
    }

//    @Override
//    public List<EmployeeResponseDTO> getAllByDepartment(String project) {
//        return employeeRepository.getEmployeeByProject(project);
//    }
}
