package org.northernarc.customerproduct.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name="product_CP")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @Min(0)
    private int stock;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItem;
}
