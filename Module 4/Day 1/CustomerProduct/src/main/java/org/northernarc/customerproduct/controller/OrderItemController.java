package org.northernarc.customerproduct.controller;
import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.OrderItemRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.service.OrderItemServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemServiceDao orderItemServiceDao;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> addOrderItem(@Valid @RequestBody OrderItemRequestDTO orderItem) {
        orderItemServiceDao.addOrderItem(orderItem);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemServiceDao.getById(id));
    }

//    @GetMapping
//    public ResponseEntity<List<OrderItemResponseDTO>> getAllOrderItems() {
//        return ResponseEntity.ok(orderItemServiceDao.getAllOrderItems());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<OrderItemResponseDTO> updateOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItemRequestDTO orderItem) {
//        orderItem.setId(Math.toIntExact(id));
//        return ResponseEntity.ok(orderItemServiceDao.updateOrderItem(orderItem));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteById(@PathVariable Long id) {
//        orderItemServiceDao.deleteById(id);
//        return ResponseEntity.ok("Order item deleted successfully");
//    }
}
