package org.northernarc.customerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDTO {
    private int id;
    private Date orderDate;
    private int customerId;
    private List<OrderItemResponseDTO> orderItems;
}
