package org.northernarc.jpaspringbootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.jpaspringbootproject.model.Project;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeResponseDTO {
    int id;
    String name;
    private List<Project> projects;
}
