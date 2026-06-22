package org.northernarc.jpaspringbootproject.controller;

import jakarta.validation.Valid;
import org.northernarc.jpaspringbootproject.model.Employee;
import org.northernarc.jpaspringbootproject.model.Project;
import org.northernarc.jpaspringbootproject.service.EmployeeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeServiceDao employeeServiceDao;
    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee){
        Employee savedEmployee = employeeServiceDao.addEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Employee>> getAll(){
        return ResponseEntity.ok(employeeServiceDao.getAll());
    }
    @GetMapping("/{id}/projects")
    public ResponseEntity<List<Project>> get(@PathVariable Long id){
        return ResponseEntity.ok(employeeServiceDao.getProjectsByEmployeeId(Math.toIntExact(id)));
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id){
        Employee employee = employeeServiceDao.getById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        employeeServiceDao.deleteById(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
    @PostMapping("/assignProject/{pid}/{eid}")
    public ResponseEntity<String> assignProject(@PathVariable Long pid, @PathVariable Long eid){
        employeeServiceDao.assignProject(pid, eid);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }

}
