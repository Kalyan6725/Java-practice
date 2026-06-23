package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.customerproduct.model.OrderItem;

import java.util.List;
@Data
@AllArgsConstructor
public class ProductResponseDTO {
    private Integer id;
    private String name;
    private String brand;
    private String category;
    private Double price;
    private int stock;
    private List<OrderItem> orderItem;
}
