package org.northernarc.customerproduct.controller;

import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.CustomerRequestDTO;
import org.northernarc.customerproduct.dto.CustomerResponseDTO;
import org.northernarc.customerproduct.service.CustomerServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerServiceDao customerServiceDao;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerServiceDao.getAllCustomers());
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> addCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.ok(customerServiceDao.addCustomer(customerRequestDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerServiceDao.getById(id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteCustomerById(@PathVariable Long id) {
        customerServiceDao.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer deleted successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> updateCustomerNameById(@PathVariable Long id, @PathVariable String name) {
        int updatedCount = customerServiceDao.updateCustomerNameById(id, name);
        return ResponseEntity.ok(updatedCount);
    }
}
