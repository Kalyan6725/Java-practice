package org.northernarc.customerproduct.controller;

import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.OrderItemCreateRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.service.OrderItemServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
public class OrderItemController {
    @Autowired
    private OrderItemServiceDao orderItemServiceDao;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> addOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemCreateRequestDTO orderItem) {
        return ResponseEntity.ok(orderItemServiceDao.addOrderItem(orderId, orderItem));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> getById(
            @PathVariable Long orderId,
            @PathVariable Long id) {
        return ResponseEntity.ok(orderItemServiceDao.getById(orderId, id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long id,
            @Valid @RequestBody OrderItemCreateRequestDTO orderItem) {
        return ResponseEntity.ok(orderItemServiceDao.updateOrderItem(orderId, id, orderItem));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> deleteById(
            @PathVariable Long orderId,
            @PathVariable Long id) {
        orderItemServiceDao.deleteById(orderId, id);
        return ResponseEntity.ok("Order item deleted successfully");
    }
}
