package org.northernarc.customerproduct.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.northernarc.customerproduct.model.Order;
import org.northernarc.customerproduct.model.Product;

public class OrderItemResponseDTO {
    int id;
    int quantity;
    private Order order;
    private Product product;
}
