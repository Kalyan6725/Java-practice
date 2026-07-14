package org.northernarc.testingassignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.testingassignment.dto.CustomerRequest;
import org.northernarc.testingassignment.dto.CustomerResponse;
import org.northernarc.testingassignment.entity.Customer;
import org.northernarc.testingassignment.exception.CustomerNotFoundException;
import org.northernarc.testingassignment.exception.DuplicateEmailException;
import org.northernarc.testingassignment.repository.CustomerRepository;
import org.northernarc.testingassignment.service.impl.CustomerServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private CustomerRequest customerRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(1L, "John Doe", "john@example.com", "encoded_pass", "9876543210");
        customerRequest = new CustomerRequest("John Doe", "john@example.com", "password123", "9876543210");
    }

    // ARRANGE-ACT-ASSERT Pattern for Customer Registration
    @Test
    void createCustomer_whenEmailIsUnique_returnsSavedCustomerResponse() {
        // Arrange
        when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_pass");
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        CustomerResponse response = customerService.createCustomer(customerRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("9876543210", response.getPhone());
        verify(passwordEncoder).encode("password123");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_whenEmailAlreadyExists_throwsDuplicateEmailException() {
        // Arrange
        when(customerRepository.existsByEmail("john@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> customerService.createCustomer(customerRequest));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_whenCustomerExists_returnsCustomerResponse() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        CustomerResponse response = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void getCustomerById_whenCustomerNotFound_throwsCustomerNotFoundException() {
        // Arrange
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(999L));
    }

    @Test
    void getAllCustomers_whenCustomersExist_returnsListOfCustomers() {
        // Arrange
        List<Customer> customers = List.of(testCustomer, new Customer(2L, "Jane Doe", "jane@example.com", "pass", "1234567890"));
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<CustomerResponse> responses = customerService.getAllCustomers();

        // Assert
        assertEquals(2, responses.size());
        assertEquals("John Doe", responses.get(0).getName());
    }

    @Test
    void updateCustomer_whenEmailChangesToExisting_throwsDuplicateEmailException() {
        // Arrange
        Customer existing = new Customer(1L, "John Doe", "john@example.com", "pass", "9876543210");
        CustomerRequest updateRequest = new CustomerRequest("John Updated", "taken@example.com", "newpass", "9876543210");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByEmail("taken@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> customerService.updateCustomer(1L, updateRequest));
    }

    @Test
    void updateCustomer_whenRequestIsValid_updatesAndReturnsResponse() {
        // Arrange
        Customer existing = new Customer(1L, "John Doe", "john@example.com", "pass", "9876543210");
        CustomerRequest updateRequest = new CustomerRequest("John Updated", "newemail@example.com", "newpass", "9876543210");
        Customer updated = new Customer(1L, "John Updated", "newemail@example.com", "encoded_pass", "9876543210");
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(passwordEncoder.encode("newpass")).thenReturn("encoded_pass");
        when(customerRepository.save(existing)).thenReturn(updated);

        // Act
        CustomerResponse response = customerService.updateCustomer(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John Updated", response.getName());
        assertEquals("newemail@example.com", response.getEmail());
    }

    @Test
    void deleteCustomer_whenCustomerExists_deletesSuccessfully() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCustomer_whenCustomerNotFound_throwsCustomerNotFoundException() {
        // Arrange
        when(customerRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(999L));
    }

    @Test
    void findByEmail_whenCustomerExists_returnsCustomer() {
        // Arrange
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testCustomer));

        // Act
        Customer result = customerService.findByEmail("john@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void findByEmail_whenCustomerNotFound_throwsCustomerNotFoundException() {
        // Arrange
        when(customerRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.findByEmail("missing@example.com"));
    }
}
