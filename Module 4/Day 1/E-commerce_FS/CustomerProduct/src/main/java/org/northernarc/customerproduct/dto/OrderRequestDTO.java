package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @PastOrPresent(message = "Order date cannot be in the future")
    private LocalDateTime orderDate;

    @NotNull(message = "Customer ID is required")
    @JsonAlias("customer_id")
    private Long customerId;

    @NotNull(message = "Order items are required")
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemCreateRequestDTO> orderItems;
}
