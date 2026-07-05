package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderSummaryDTO {
    int id;
    Date orderDate;
    private List<OrderItemResponseDTO> orderItems;
}
