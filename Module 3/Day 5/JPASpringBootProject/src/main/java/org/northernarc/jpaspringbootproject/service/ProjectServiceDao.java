package org.northernarc.jpaspringbootproject.service;

import org.northernarc.jpaspringbootproject.dto.ProjectRequestDTO;
import org.northernarc.jpaspringbootproject.dto.ProjectResponseDTO;
import org.northernarc.jpaspringbootproject.model.Project;

import java.util.List;

public interface ProjectServiceDao {
    public ProjectResponseDTO addProject(ProjectRequestDTO projectRequestDTO);
    public List<ProjectResponseDTO> getAll();
    public ProjectResponseDTO getById(Long id);
    public void deleteById(Long id);
}
