package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private Long customerId;
    private List<OrderItemResponseDTO> orderItems;
}
