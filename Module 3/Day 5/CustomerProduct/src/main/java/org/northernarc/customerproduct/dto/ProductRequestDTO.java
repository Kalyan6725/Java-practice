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

    @Valid
    private List<OrderItem> orderItem;
}
