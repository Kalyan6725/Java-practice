package org.northernarc.customerproduct.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product brand is required")
    private String brand;

    @NotBlank(message = "Product category is required")
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

}
