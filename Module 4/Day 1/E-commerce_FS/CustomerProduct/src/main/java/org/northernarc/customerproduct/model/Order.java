package org.northernarc.customerproduct.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

import java.util.List;

@Entity
@Data
@Table(name="orders_CP")
public class Order {
    @Id
    @GeneratedValue()
    private Long id;

    @PastOrPresent
    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.CREATED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public enum OrderStatus {
        CREATED,
        CONFIRMED,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
}
