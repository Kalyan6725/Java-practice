package org.northernarc.customerproduct.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Table(name="orderitem_CP")
public class OrderItem {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    int id;

    @Positive
    @Min(1)
    int quantity;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Order order;

    @ManyToOne
    private Product product;
}
