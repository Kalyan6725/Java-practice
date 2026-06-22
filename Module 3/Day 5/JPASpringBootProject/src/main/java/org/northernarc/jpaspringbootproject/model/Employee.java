package org.northernarc.jpaspringbootproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@ToString
//@RequiredArgsConstructor
//@Data//equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
@Getter @Setter @NoArgsConstructor
@Table(name="employee_jpa")
public class Employee {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    int id;
    String name;
    String email;
    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    private List<Project> projects;
}
