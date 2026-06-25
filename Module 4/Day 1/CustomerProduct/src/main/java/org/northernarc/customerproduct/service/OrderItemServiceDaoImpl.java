package org.northernarc.customerproduct.service;

import org.northernarc.customerproduct.dto.OrderItemRequestDTO;
import org.northernarc.customerproduct.dto.OrderItemResponseDTO;
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
        Order order = orderRepository.findById(orderItemRequestDTO.getOrderId()).orElse(null);
        Product product = productRepository.findById(orderItemRequestDTO.getProductId()).orElse(null);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItemResponseDTO getById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(Math.toIntExact(id)).orElse(null);
        return new OrderItemResponseDTO(orderItem.getId(), orderItem.getQuantity(), orderItem.getOrder(), orderItem.getProduct());
    }

    @Override
    public OrderItemResponseDTO updateOrderItem(int id, OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);
        if (orderItem != null) {
            orderItem.setQuantity(orderItemRequestDTO.getQuantity());
            Order order = orderRepository.findById(orderItemRequestDTO.getOrderId()).orElse(null);
            Product product = productRepository.findById(orderItemRequestDTO.getProductId()).orElse(null);
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
            return new OrderItemResponseDTO(updatedOrderItem.getId(), updatedOrderItem.getQuantity(), updatedOrderItem.getOrder(), updatedOrderItem.getProduct());
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
                .map(orderItem -> new OrderItemResponseDTO(orderItem.getId(), orderItem.getQuantity(), orderItem.getOrder(), orderItem.getProduct()))
                .toList();
    }
}