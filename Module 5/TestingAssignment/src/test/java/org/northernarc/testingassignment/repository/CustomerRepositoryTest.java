package org.northernarc.testingassignment.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.northernarc.testingassignment.entity.Customer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(null, "John Doe", "john@test.com", "pass123", "9876543210");
    }

    @Test
    void save_whenCustomerIsValid_persistsCustomer() {
        // Arrange & Act
        Customer saved = customerRepository.saveAndFlush(testCustomer);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("john@test.com", saved.getEmail());
    }

    @Test
    void findByEmail_whenEmailExists_returnsCustomer() {
        // Arrange
        customerRepository.saveAndFlush(testCustomer);

        // Act
        var result = customerRepository.findByEmail("john@test.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void existsByEmail_whenEmailExists_returnsTrue() {
        // Arrange
        customerRepository.saveAndFlush(testCustomer);

        // Act & Assert
        assertTrue(customerRepository.existsByEmail("john@test.com"));
        assertFalse(customerRepository.existsByEmail("missing@test.com"));
    }

    @Test
    void save_whenEmailIsDuplicated_throwsDataIntegrityViolationException() {
        // Arrange
        customerRepository.saveAndFlush(testCustomer);
        Customer duplicate = new Customer(null, "Jane Doe", "john@test.com", "pass456", "1234567890");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> 
            customerRepository.saveAndFlush(duplicate)
        );
    }

    @Test
    void delete_whenCustomerExists_deletesSuccessfully() {
        // Arrange
        Customer saved = customerRepository.saveAndFlush(testCustomer);

        // Act
        customerRepository.deleteById(saved.getId());

        // Assert
        assertFalse(customerRepository.findById(saved.getId()).isPresent());
    }
}
