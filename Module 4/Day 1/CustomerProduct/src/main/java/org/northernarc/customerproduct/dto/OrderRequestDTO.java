package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @PastOrPresent(message = "Order date cannot be in the future")
    private Date orderDate;

    @NotNull(message = "Customer ID is required")
    @JsonAlias("customer_id")
    private Integer customerId;

    @NotNull(message = "Order items are required")
    @Valid
    private List<OrderItemCreateRequestDTO> orderItems;
}
