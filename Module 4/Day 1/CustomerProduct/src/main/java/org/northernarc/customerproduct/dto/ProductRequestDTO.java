package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.customerproduct.model.OrderItem;

import java.util.List;
@Data
@AllArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product brand is required")
    private String brand;

    @NotBlank(message = "Product category is required")
    private String category;

    @Positive(message = "Price must be greater than 0")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

}
