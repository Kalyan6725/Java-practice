package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.northernarc.customerproduct.model.Order;
import org.northernarc.customerproduct.model.Product;
@Data
@AllArgsConstructor
public class OrderItemRequestDTO {

    @Positive
    @Min(1)
    @NotNull
    int quantity;

    //private Order order;
    private int orderId;

//    @NotNull
//    private Product product;
    @NotNull
    private int productId;
}
