package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderItemCreateRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.exceptions.ProductNotFound;
import org.northernarc.customerproduct.exceptions.ValidationFailedException;
import org.northernarc.customerproduct.model.Order;
import org.northernarc.customerproduct.model.OrderItem;
import org.northernarc.customerproduct.model.Product;
import org.northernarc.customerproduct.model.User;
import org.northernarc.customerproduct.repository.OrderItemRepository;
import org.northernarc.customerproduct.repository.OrderRepository;
import org.northernarc.customerproduct.repository.ProductRepository;
import org.northernarc.customerproduct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemServiceDaoImpl implements OrderItemServiceDao {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public OrderItemResponseDTO addOrderItem(Long orderId, OrderItemCreateRequestDTO orderItemRequestDTO) {
        Order order = getOrderWithAccess(orderId);
        Product product = productRepository.findById(orderItemRequestDTO.getProductId())
                .orElseThrow(() -> new ProductNotFound("no product found with id " + orderItemRequestDTO.getProductId()));
        if (orderItemRequestDTO.getQuantity() > product.getStock()) {
            throw new ValidationFailedException("Insufficient stock for product id " + product.getId());
        }
        product.setStock(product.getStock() - orderItemRequestDTO.getQuantity());

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        orderItem.setOrder(order);
        orderItem.setProduct(product);

        return mapToOrderItemResponse(orderItemRepository.save(orderItem));
    }

    @Override
    public OrderItemResponseDTO getById(Long orderId, Long id) {
        Order order = getOrderWithAccess(orderId);
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(id, order.getId())
                .orElseThrow(() -> new OrderNotFound("no order item found with id " + id + " for order " + orderId));
        return mapToOrderItemResponse(orderItem);
    }

    @Override
    @Transactional
    public OrderItemResponseDTO updateOrderItem(Long orderId, Long id, OrderItemCreateRequestDTO orderItemRequestDTO) {
        Order order = getOrderWithAccess(orderId);
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(id, order.getId())
                .orElseThrow(() -> new OrderNotFound("no order item found with id " + id + " for order " + orderId));

        Product product = productRepository.findById(orderItemRequestDTO.getProductId())
                .orElseThrow(() -> new ProductNotFound("no product found with id " + orderItemRequestDTO.getProductId()));
        Product currentProduct = orderItem.getProduct();
        currentProduct.setStock(currentProduct.getStock() + orderItem.getQuantity());
        if (orderItemRequestDTO.getQuantity() > product.getStock()) {
            throw new ValidationFailedException("Insufficient stock for product id " + product.getId());
        }
        product.setStock(product.getStock() - orderItemRequestDTO.getQuantity());

        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        orderItem.setProduct(product);
        return mapToOrderItemResponse(orderItemRepository.save(orderItem));
    }

    @Override
    @Transactional
    public void deleteById(Long orderId, Long id) {
        Order order = getOrderWithAccess(orderId);
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(id, order.getId())
                .orElseThrow(() -> new OrderNotFound("no order item found with id " + id + " for order " + orderId));
        Product product = orderItem.getProduct();
        product.setStock(product.getStock() + orderItem.getQuantity());
        orderItemRepository.delete(orderItem);
    }

    private Order getOrderWithAccess(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound("no order found with id " + orderId));

        if (!isAdmin() && !order.getUser().getId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("You are not allowed to access this order");
        }
        return order;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("Invalid authentication context");
        }
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("No logged-in user found: " + authentication.getName()));
        return user.getId();
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    private OrderItemResponseDTO mapToOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponseDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId());
    }
}
