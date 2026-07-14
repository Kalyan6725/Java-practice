package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderSummaryDTO {
    Long id;
    LocalDateTime orderDate;
    private List<OrderItemResponseDTO> orderItems;
}
