package org.northernarc.jpaspringbootproject.controller;

import jakarta.validation.Valid;
import org.northernarc.jpaspringbootproject.model.Project;
import org.northernarc.jpaspringbootproject.service.ProjectServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectServiceDao projectServiceDao;

    @GetMapping("")
    public ResponseEntity<List<Project>> getAll(){
        return ResponseEntity.ok(projectServiceDao.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable Long id){
        Project project = projectServiceDao.getById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("")
    public ResponseEntity<Project> addProject(@Valid @RequestBody Project project) {
        Project savedProject = projectServiceDao.addProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        projectServiceDao.deleteById(id);
        return ResponseEntity.ok("Project deleted successfully");
    }
}
