package org.northernarc.jpaspringbootproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
// @Data
@Getter
@Setter
@NoArgsConstructor
@Table(name="project_jpa")
public class Project {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    int id;
    String name;
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;
}
