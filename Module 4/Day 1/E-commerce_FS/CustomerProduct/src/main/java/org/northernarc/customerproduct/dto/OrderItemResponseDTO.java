package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private Integer quantity;
    private Long orderId;
    private Long productId;
}
