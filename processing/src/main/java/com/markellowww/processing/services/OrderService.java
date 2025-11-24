package com.markellowww.processing.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.markellowww.processing.dto.OrderDto;
import com.markellowww.processing.exceptions.OrderCreationException;
import com.markellowww.processing.exceptions.OrderDeserializationException;
import com.markellowww.processing.exceptions.OrderFindingException;
import com.markellowww.processing.exceptions.OrderSerializationException;
import com.markellowww.processing.models.Order;
import com.markellowww.processing.models.mappers.OrderMapper;
import com.markellowww.processing.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Markelloww
 */

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;

    public void createOrder(OrderDto orderDto) {
        logger.info("Starting order creation process for customer: {}", orderDto.getCustomerId());
        logger.debug("Order DTO received: {}", orderDto);

        try {
            Order order = orderMapper.toEntity(orderDto);
            logger.debug("Order entity mapped successfully");

            if (order.getItems() != null) {
                logger.info("Processing {} items for order", order.getItems().size());
                order.getItems().forEach(item -> {
                    item.setOrder(order);
                    logger.debug("Linked item: {} (quantity: {}) to order",
                            item.getProductName(), item.getQuantity());
                });
            } else {
                logger.warn("No items found in the order for customer: {}", orderDto.getCustomerId());
            }

            logger.info("Saving order to database...");
            Order savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with generated ID: {}", savedOrder.getOrderId());
            logger.debug("Saved order details: {}", savedOrder);

        } catch (Exception e) {
            throw new OrderCreationException(
                    String.format("Error occurred while creating order for customer %s: ",
                            orderDto.getCustomerId()), e);
        }
    }

    @Transactional(readOnly = true)
    public OrderDto getOrder(Long orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        try {
            return orderRepository.findById(orderId)
                    .map(order -> {
                        logger.debug("Order found: {}", order);
                        OrderDto dto = orderMapper.toDto(order);
                        logger.info("Successfully retrieved order ID: {} for customer: {}",
                                orderId, dto.getCustomerId());
                        return dto;
                    })
                    .orElseThrow(() -> {
                        logger.warn("Order not found with ID: {}", orderId);
                        return new EntityNotFoundException("Order not found with ID: " + orderId);
                    });

        } catch (Exception e) {
            throw new OrderFindingException(String.format("Error occurred while fetching order %s: ", orderId), e);
        }
    }

    public String serializeOrderDto(OrderDto orderDto) {
        try {
            logger.debug("Starting to serialize the received JSON order");
            String orderJson = objectMapper.writeValueAsString(orderDto);
            logger.debug("Order {} was successfully serialized", orderDto.getOrderId());
            return orderJson;
        } catch (JsonProcessingException e) {
            throw new OrderSerializationException("Failed to serialize order %s".formatted(orderDto.getOrderId()), e);
        }
    }

    public OrderDto deserializeOrderDto(String orderJson) {
        try {
            logger.debug("Starting to deserialize the received order");
            OrderDto orderDto = objectMapper.readValue(orderJson, OrderDto.class);
            logger.debug("Order {} was successfully deserialized", orderDto.getOrderId());
            return orderDto;
        } catch (JsonProcessingException e) {
            throw new OrderDeserializationException("Failed to deserialize order %s".formatted(orderJson), e);
        }
    }
}