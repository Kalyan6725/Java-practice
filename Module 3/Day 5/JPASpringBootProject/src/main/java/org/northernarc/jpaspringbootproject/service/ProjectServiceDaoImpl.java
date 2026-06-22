package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.model.Employee;
import org.northernarc.jpaspringbootproject.model.Project;
import org.northernarc.jpaspringbootproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectServiceDaoImpl implements ProjectServiceDao {
    @Autowired
    ProjectRepository projectRepository;

    @Override
    public Project addProject(Project project) {
        return projectRepository.save(project);
    }
    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project getById(Long id) {
        
        return projectRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        projectRepository.deleteById(Math.toIntExact(id));
    }
}
