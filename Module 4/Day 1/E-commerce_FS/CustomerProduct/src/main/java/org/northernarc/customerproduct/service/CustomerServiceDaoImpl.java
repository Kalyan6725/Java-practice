package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.CustomerRequestDTO;
import org.northernarc.customerproduct.dto.CustomerResponseDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.dto.OrderSummaryDTO;
import org.northernarc.customerproduct.dto.RegisterRequestDTO;
import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.exceptions.UsernameAlreadyExistsException;
import org.northernarc.customerproduct.exceptions.ValidationFailedException;
import org.northernarc.customerproduct.model.Order;
import org.northernarc.customerproduct.model.User;
import org.northernarc.customerproduct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceDaoImpl implements CustomerServiceDao {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO) {
        ensureUsernameAvailable(customerRequestDTO.getUsername());

        User user = new User();
        user.setName(customerRequestDTO.getName());
        user.setUsername(customerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(customerRequestDTO.getPassword()));
        user.setRole(parseRole(customerRequestDTO.getRole()));

        User savedUser = userRepository.save(user);
        return mapToCustomerResponse(savedUser);
    }

    @Override
    @Transactional
    public CustomerResponseDTO registerCustomer(RegisterRequestDTO registerRequestDTO) {
        ensureUsernameAvailable(registerRequestDTO.getUsername());

        User user = new User();
        user.setName(registerRequestDTO.getName());
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(User.Role.USER);

        User savedUser = userRepository.save(user);
        return mapToCustomerResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFound("no customer found with id " + id));
        return mapToCustomerResponse(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomerNotFound("no customer found with id " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return userRepository.findAll().stream()
                .map(this::mapToCustomerResponse)
                .toList();
    }

    @Override
    @Transactional
    public int updateCustomerNameById(Long id, String name) {
        return userRepository.updateUserName(id, name);
    }

    private void ensureUsernameAvailable(String username) {
        if (userRepository.findByUsernameIgnoreCase(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists: " + username);
        }
    }

    private CustomerResponseDTO mapToCustomerResponse(User user) {
        return new CustomerResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole().name(),
                mapToOrderResponse(user.getOrders()));
    }

    private User.Role parseRole(String role) {
        try {
            return User.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationFailedException("Role must be ADMIN or USER");
        }
    }

    private List<OrderSummaryDTO> mapToOrderResponse(List<Order> orders) {
        if (orders == null) {
            return List.of();
        }
        return orders.stream()
                .map(order -> new OrderSummaryDTO(order.getId(), order.getOrderDate(),
                        order.getOrderItems() == null ? List.<OrderItemResponseDTO>of() : order.getOrderItems().stream()
                                .map(item -> new OrderItemResponseDTO(
                                        item.getId(),
                                        item.getQuantity(),
                                        order.getId(),
                                        item.getProduct().getId()))
                                .toList()))
                .toList();
    }
}
