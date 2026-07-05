package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderItemRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
import org.northernarc.customerproduct.exceptions.OrderNotFound;
import org.northernarc.customerproduct.exceptions.ProductNotFound;
import org.northernarc.customerproduct.model.Order;
import org.northernarc.customerproduct.model.OrderItem;
import org.northernarc.customerproduct.model.Product;
import org.northernarc.customerproduct.repository.OrderItemRepository;
import org.northernarc.customerproduct.repository.OrderRepository;
import org.northernarc.customerproduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemServiceDaoImpl implements OrderItemServiceDao {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addOrderItem(OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem orderItem=new OrderItem();
        orderItem.setQuantity(orderItemRequestDTO.getQuantity());
        Order order = orderRepository.findById(orderItemRequestDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFound("no order found with id " + orderItemRequestDTO.getOrderId()));
        Product product = productRepository.findById(orderItemRequestDTO.getProductId())
                .orElseThrow(() -> new ProductNotFound("no product found with id " + orderItemRequestDTO.getProductId()));
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItemResponseDTO getById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(Math.toIntExact(id)).orElse(null);
        return mapToOrderItemResponse(orderItem);
    }

    @Override
    public OrderItemResponseDTO updateOrderItem(int id, OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);
        if (orderItem != null) {
            orderItem.setQuantity(orderItemRequestDTO.getQuantity());
            Order order = orderRepository.findById(orderItemRequestDTO.getOrderId())
                    .orElseThrow(() -> new OrderNotFound("no order found with id " + orderItemRequestDTO.getOrderId()));
            Product product = productRepository.findById(orderItemRequestDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFound("no product found with id " + orderItemRequestDTO.getProductId()));
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
            return mapToOrderItemResponse(updatedOrderItem);
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        orderItemRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public List<OrderItemResponseDTO> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                .map(this::mapToOrderItemResponse)
                .toList();
    }

    private OrderItemResponseDTO mapToOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponseDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId());
    }
}