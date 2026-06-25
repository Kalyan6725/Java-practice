package org.northernarc.customerproduct.controller;
import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;
import org.northernarc.customerproduct.service.OrderServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderServiceDao orderServiceDao;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> addOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.ok(orderServiceDao.addOrder(orderRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderServiceDao.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderServiceDao.getAllOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequestDTO orderRequestDTO) {

        return ResponseEntity.ok(orderServiceDao.updateOrder(Math.toIntExact(id),orderRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        orderServiceDao.deleteById(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
