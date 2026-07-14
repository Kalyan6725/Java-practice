package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String brand;
    private String category;
    private BigDecimal price;
    private int stock;
}
