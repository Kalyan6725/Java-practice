package org.northernarc.customerproduct.controller;
import org.northernarc.customerproduct.model.OrderItem;
import org.northernarc.customerproduct.service.OrderItemServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemServiceDao orderItemServiceDao;

    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(@RequestBody OrderItem orderItem) {
        return ResponseEntity.ok(orderItemServiceDao.addOrderItem(orderItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemServiceDao.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemServiceDao.getAllOrderItems());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long id, @RequestBody OrderItem orderItem) {
        orderItem.setId(id);
        return ResponseEntity.ok(orderItemServiceDao.updateOrderItem(orderItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        orderItemServiceDao.deleteById(id);
        return ResponseEntity.ok("Order item deleted successfully");
    }
}
