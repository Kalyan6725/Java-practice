package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderItemCreateRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.dto.OrderRequestDTO;
import org.northernarc.customerproduct.dto.OrderResponseDTO;
import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.exceptions.ProductNotFound;
import org.northernarc.customerproduct.exceptions.ValidationFailedException;
import org.northernarc.customerproduct.model.OrderItem;
import org.northernarc.customerproduct.model.Product;
import org.northernarc.customerproduct.model.User;
import org.northernarc.customerproduct.repository.OrderRepository;
import org.northernarc.customerproduct.repository.ProductRepository;
import org.northernarc.customerproduct.repository.UserRepository;
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
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
     @Override
    public void deleteById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));
        restoreStock(order.getOrderItems());
        orderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getById(Long id) {
        // keep session open for lazy associations (orderItems -> product) while mapping DTO
        Order order=orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));
        assertCanAccessOrder(order);
        return mapToOrderResponse(order);
     }

    @Override
    @Transactional
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));

        order.setOrderDate(orderRequestDTO.getOrderDate());
        restoreStock(order.getOrderItems());
        order.setUser(userRepository.findById(orderRequestDTO.getCustomerId())
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
                : orderRepository.findByUserId(getCurrentCustomerId());
        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();

        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(Order.OrderStatus.CREATED);
        Long customerId = isAdmin() ? orderRequestDTO.getCustomerId() : getCurrentCustomerId();
        order.setUser(userRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFound("no customer found with id " + customerId)));
        order.setOrderItems(mapToOrderItems(orderRequestDTO.getOrderItems(), order));

        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFound("no order found with id " + id));
        assertCanAccessOrder(order);

        if (order.getStatus() == Order.OrderStatus.SHIPPED || order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new ValidationFailedException("Shipped or delivered orders cannot be cancelled");
        }
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new ValidationFailedException("Order is already cancelled");
        }

        restoreStock(order.getOrderItems());
        order.setStatus(Order.OrderStatus.CANCELLED);
        return mapToOrderResponse(orderRepository.save(order));
    }

    private Long getCurrentCustomerId() {
        String username = getCurrentUsername();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("No logged-in user found: " + username));
        return user.getId();
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

    private void assertCanAccessOrder(Order order) {
        if (!isAdmin() && !order.getUser().getId().equals(getCurrentCustomerId())) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }
    }

    private List<OrderItem> mapToOrderItems(List<OrderItemCreateRequestDTO> items, Order order) {
        return items.stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new ProductNotFound("no product found with id " + itemDto.getProductId()));
                    if (itemDto.getQuantity() > product.getStock()) {
                        throw new ValidationFailedException("Insufficient stock for product id " + product.getId());
                    }
                    product.setStock(product.getStock() - itemDto.getQuantity());
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setProduct(product);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();
    }

    private void restoreStock(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return;
        }
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() + orderItem.getQuantity());
        }
    }

    private OrderResponseDTO mapToOrderResponse(Order order) {
        List<OrderItemResponseDTO> orderItemResponses = (order.getOrderItems() == null ? List.<OrderItemResponseDTO>of() : order.getOrderItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getQuantity(),
                        order.getId(),
                        item.getProduct().getId()))
                .toList());
        return new OrderResponseDTO(
                order.getId(),
                order.getOrderDate(),
                order.getStatus().name(),
                order.getUser().getId(),
                orderItemResponses);
    }
}
