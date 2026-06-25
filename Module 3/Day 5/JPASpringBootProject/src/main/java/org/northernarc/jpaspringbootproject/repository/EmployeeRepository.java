package org.northernarc.jpaspringbootproject.repository;

import org.northernarc.jpaspringbootproject.dto.EmployeeResponseDTO;
import org.northernarc.jpaspringbootproject.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
//    @Query("""
//        SELECT new org.northernarc.jpaspringbootproject.dto.EmployeeResponseDTO(e.id,e.name)
//        FROM Employee e JOIN e.projects p WHERE p.id = :projectId
//        """)
//    List<EmployeeResponseDTO> getEmployeeByProject(@Param("project") project);
}
