package org.northernarc.jpaspringbootproject.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.jpaspringbootproject.model.Employee;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectRequestDTO {

    @NotBlank
    String name;

    @Valid
    private List<Employee> employees;
}
