package org.northernarc.customerproduct.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.customerproduct.model.OrderItem;

import java.sql.Date;
import java.util.List;
@Data
@AllArgsConstructor
public class OrderRequestDTO {

    @PastOrPresent
    private Date orderDate;

//    @NotNull
//    private Customer customer;
    private int customer_id;

    @NotNull
    private List<OrderItem> orderItems;
}
