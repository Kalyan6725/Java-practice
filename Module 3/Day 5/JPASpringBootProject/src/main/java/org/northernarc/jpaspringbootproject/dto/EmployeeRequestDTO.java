package org.northernarc.jpaspringbootproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.jpaspringbootproject.model.Project;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeRequestDTO {

    @NotBlank
    String name;

    @Email
    @NotBlank
    String email;

    @Valid
    private List<Project> projects;
}
