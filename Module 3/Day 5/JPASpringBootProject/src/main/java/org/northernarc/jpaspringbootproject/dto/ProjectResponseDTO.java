package org.northernarc.jpaspringbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.jpaspringbootproject.model.Employee;

import java.util.List;
@Data
@AllArgsConstructor
public class ProjectResponseDTO {
    int id;
    String name;
    private List<Employee> employees;
}
