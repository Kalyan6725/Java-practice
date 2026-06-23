package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.dto.ProjectRequestDTO;
import org.northernarc.jpaspringbootproject.dto.ProjectResponseDTO;
import org.northernarc.jpaspringbootproject.model.Employee;
import org.northernarc.jpaspringbootproject.model.Project;
import org.northernarc.jpaspringbootproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectServiceDaoImpl implements ProjectServiceDao {
    @Autowired
    ProjectRepository projectRepository;

    @Override
    public ProjectResponseDTO addProject(ProjectRequestDTO projectRequestDTO) {
        Project project = new Project();
        project.setName(projectRequestDTO.getName());
        project.setEmployees(projectRequestDTO.getEmployees());
        Project savedProject = projectRepository.save(project);
        return new ProjectResponseDTO(savedProject.getId(), savedProject.getName(), savedProject.getEmployees());
    }
    @Override
    public List<ProjectResponseDTO> getAll() {

        List<Project> projects = projectRepository.findAll(Sort.by("name").descending());
        return projects.stream()
                .map(project -> new ProjectResponseDTO(project.getId(), project.getName(), project.getEmployees()))
                .toList();
    }

    @Override
    public ProjectResponseDTO getById(Long id) {
        
        Project project = projectRepository.findById(Math.toIntExact(id)).orElse(null);
        if (project != null) {
            return new ProjectResponseDTO(project.getId(), project.getName(), project.getEmployees());
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        projectRepository.deleteById(Math.toIntExact(id));
    }
}
