package org.northernarc.jpaspringbootproject.controller;

import jakarta.validation.Valid;
import org.northernarc.jpaspringbootproject.dto.EmployeeRequestDTO;
import org.northernarc.jpaspringbootproject.dto.EmployeeResponseDTO;
import org.northernarc.jpaspringbootproject.dto.ProjectResponseDTO;
import org.northernarc.jpaspringbootproject.service.EmployeeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeServiceDao employeeServiceDao;
    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO){
        EmployeeResponseDTO savedEmployeeResponseDTO = employeeServiceDao.addEmployee(employeeRequestDTO);
        return ResponseEntity.ok(savedEmployeeResponseDTO);
    }
//    @GetMapping("/project/{project}")
//    public List<EmployeeResponseDTO> getAllEmployees(@PathVariable String project){
//        return employeeServiceDao.getAllByDepartment(project);
//    }
    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeResponseDTO>> getAll(){
        return ResponseEntity.ok(employeeServiceDao.getAll());
    }
    @GetMapping("/{id}/projects")
    public ResponseEntity<List<ProjectResponseDTO>> get(@PathVariable Long id){
        return ResponseEntity.ok(employeeServiceDao.getProjectsByEmployeeId(Math.toIntExact(id)));
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable Long id){
        EmployeeResponseDTO employeeResponseDTO = employeeServiceDao.getById(id);
        if (employeeResponseDTO != null) {
            return ResponseEntity.ok(employeeResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//    @DeleteMapping("/deleteById/{id}")
//    public ResponseEntity<String> deleteById(@PathVariable Long id){
//        employeeServiceDao.deleteById(id);
//        return ResponseEntity.ok("Employee deleted successfully");
//    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Map<String, String>> deleteById(@PathVariable Long id) {
        employeeServiceDao.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/assignProject/{pid}/{eid}")
    public ResponseEntity<String> assignProject(@PathVariable Long pid, @PathVariable Long eid){
        employeeServiceDao.assignProject(pid, eid);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }
    @GetMapping("/page/{page}/{size}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByPage(@PathVariable int page, @PathVariable int size) {
        List<EmployeeResponseDTO> employees = employeeServiceDao.getEmployeesByPage(page, size);
        return ResponseEntity.ok(employees);
    }

}
