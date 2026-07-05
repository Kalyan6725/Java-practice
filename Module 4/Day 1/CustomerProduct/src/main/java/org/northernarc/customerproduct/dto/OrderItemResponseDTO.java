package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponseDTO {
    private int id;
    private int quantity;
    private int orderId;
    private int productId;
}
