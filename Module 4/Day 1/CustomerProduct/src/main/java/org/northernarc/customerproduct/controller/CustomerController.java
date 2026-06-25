package org.northernarc.customerproduct.controller;

import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.CustomerRequestDTO;
import org.northernarc.customerproduct.dto.CustomerResponseDTO;
import org.northernarc.customerproduct.model.Customer;
import org.northernarc.customerproduct.service.CustomerServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerServiceDao.getAllCustomers());
    }

    @PostMapping("")
    public ResponseEntity<CustomerResponseDTO> addCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.ok(customerServiceDao.addCustomer(customerRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable int id) {
        return ResponseEntity.ok(customerServiceDao.getById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCustomerById(@PathVariable int id) {
        customerServiceDao.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer deleted successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}/{name}")
    public ResponseEntity<Integer> updateCustomerNameById(@PathVariable int id, @PathVariable String name) {
        int updatedCount = customerServiceDao.updateCustomerNameById(id, name);
        return ResponseEntity.ok(updatedCount);
    }
}
