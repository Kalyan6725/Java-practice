package org.northernarc.jpaspringbootproject.controller;

import jakarta.validation.Valid;
import org.northernarc.jpaspringbootproject.dto.ProjectRequestDTO;
import org.northernarc.jpaspringbootproject.dto.ProjectResponseDTO;
import org.northernarc.jpaspringbootproject.model.Project;
import org.northernarc.jpaspringbootproject.service.ProjectServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectServiceDao projectServiceDao;

    @GetMapping("")
    public ResponseEntity<List<ProjectResponseDTO>> getAll(){
        return ResponseEntity.ok(projectServiceDao.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getById(@PathVariable Long id){
        ProjectResponseDTO project = projectServiceDao.getById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("")
    public ResponseEntity<ProjectResponseDTO> addProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        return ResponseEntity.ok(projectServiceDao.addProject(projectRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        projectServiceDao.deleteById(id);
        return ResponseEntity.ok("Project deleted successfully");
    }
}
