package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.northernarc.customerproduct.model.Order;
import org.northernarc.customerproduct.model.Product;

public class OrderItemRequestDTO {

    @Positive
    @Min(1)
    @NotNull
    int quantity;

    private Order order;

    @NotNull
    private Product product;
}
