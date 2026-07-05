package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderItemCreateRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;
import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.exceptions.ProductNotFound;
import org.northernarc.customerproduct.model.Customer;
import org.northernarc.customerproduct.model.JpaUser;
import org.northernarc.customerproduct.model.OrderItem;
import org.northernarc.customerproduct.model.Product;
import org.northernarc.customerproduct.repository.CustomerRepository;
import org.northernarc.customerproduct.repository.JpaUserRepo;
import org.northernarc.customerproduct.repository.OrderRepository;
import org.northernarc.customerproduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.northernarc.customerproduct.model.Order;

import java.util.List;

@Service
public class OrderServiceDaoImpl implements OrderServiceDao {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    JpaUserRepo jpaUserRepo;
    @Autowired
    ProductRepository productRepository;
     @Override
    public void deleteById(Long id) {
        if (!orderRepository.existsById(Math.toIntExact(id))) {
            throw new OrderNotFound("no order found with id " + id);
        }
        orderRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getById(Long id) {
        // keep session open for lazy associations (orderItems -> product) while mapping DTO
        Order order=orderRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));
        if (!isAdmin() && order.getCustomer().getId() != getCurrentCustomerId()) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }
        return mapToOrderResponse(order);
     }

    @Override
    @Transactional
    public OrderResponseDTO updateOrder(int id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));

        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setCustomer(customerRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new CustomerNotFound("no customer found with id " + orderRequestDTO.getCustomerId())));
        order.setOrderItems(mapToOrderItems(orderRequestDTO.getOrderItems(), order));

        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = isAdmin()
                ? orderRepository.findAll()
                : orderRepository.findByCustomerId(getCurrentCustomerId());
        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();

        order.setOrderDate(orderRequestDTO.getOrderDate());
        Integer customerId = isAdmin() ? orderRequestDTO.getCustomerId() : getCurrentCustomerId();
        order.setCustomer(customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFound("no customer found with id " + customerId)));
        order.setOrderItems(mapToOrderItems(orderRequestDTO.getOrderItems(), order));

        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    private Integer getCurrentCustomerId() {
        String username = getCurrentUsername();
        JpaUser user = jpaUserRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("No logged-in user found: " + username));
        if (user.getCustomer() == null) {
            throw new AccessDeniedException("No customer is linked to logged-in user: " + username);
        }
        return user.getCustomer().getId();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("Invalid authentication context");
        }
        return authentication.getName();
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    private List<OrderItem> mapToOrderItems(List<OrderItemCreateRequestDTO> items, Order order) {
        return items.stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new ProductNotFound("no product found with id " + itemDto.getProductId()));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setProduct(product);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();
    }

    private OrderResponseDTO mapToOrderResponse(Order order) {
        List<OrderItemResponseDTO> orderItemResponses = order.getOrderItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getQuantity(),
                        order.getId(),
                        item.getProduct().getId()))
                .toList();
        return new OrderResponseDTO(order.getId(), order.getOrderDate(), order.getCustomer().getId(), orderItemResponses);
    }
}
