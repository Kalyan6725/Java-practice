package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.dto.EmployeeRequestDTO;
import org.northernarc.jpaspringbootproject.dto.EmployeeResponseDTO;
import org.northernarc.jpaspringbootproject.dto.ProjectResponseDTO;
import org.northernarc.jpaspringbootproject.model.Employee;
import org.northernarc.jpaspringbootproject.model.Project;

import java.util.List;

public interface EmployeeServiceDao {
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employeeRequestDTO);
    public List<EmployeeResponseDTO> getAll();
    public EmployeeResponseDTO getById(Long id);
    public void deleteById(Long id);
    public void assignProject(Long pid, Long eid);
    public List<ProjectResponseDTO> getProjectsByEmployeeId(int id);

    List<EmployeeResponseDTO> getEmployeesByPage(int page, int size);
}
