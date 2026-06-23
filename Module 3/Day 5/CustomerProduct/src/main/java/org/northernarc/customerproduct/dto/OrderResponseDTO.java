package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.customerproduct.model.Customer;
import org.northernarc.customerproduct.model.OrderItem;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDTO {
    private int id;
    private Date orderDate;
    @JsonIgnore
    private Customer customer;
    private List<OrderItem> orderItems;
}
