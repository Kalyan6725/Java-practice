package org.northernarc.customerproduct.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="product_CP")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @Positive
    @Min(0)
    private Double price;

    @Min(0)
    private int stock;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItem;
}
