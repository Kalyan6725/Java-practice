package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.model.Project;

import java.util.List;

public interface ProjectServiceDao {
    public Project addProject(Project project);
    public List<Project> getAll();
    public Project getById(Long id);
    public void deleteById(Long id);
}
