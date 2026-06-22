package org.northernarc.customerproduct.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="customer_CP")
public class Customer {
    @Id
    @GeneratedValue()
    private int id;
    @NotBlank
    private String name;
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Order> orders;
}
