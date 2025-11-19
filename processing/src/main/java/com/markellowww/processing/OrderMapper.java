package com.markellowww.processing;

import com.markellowww.processing.dto.AddressDto;
import com.markellowww.processing.dto.OrderDto;
import com.markellowww.processing.dto.OrderItemDto;
import com.markellowww.processing.models.Order;
import com.markellowww.processing.models.OrderItem;
import com.markellowww.processing.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Markelloww
 */

@Component
public class OrderMapper {
    private static final Logger logger = LoggerFactory.getLogger(OrderMapper.class);

    public Order toEntity(OrderDto dto) {
        logger.debug("Starting toEntity conversion for OrderDto: {}", dto);
        if (dto == null) {
            logger.warn("OrderDto is null, returning null entity");
            return null;
        }

        try {
            Order order = Order.builder()
                    .customerId(dto.getCustomerId())
                    .status(dto.getStatus())
                    .shippingType(dto.getShippingType())
                    .timestamp(dto.getTimestamp())
                    .processedAt(dto.getProcessedAt())
                    .deliveryAddress(extractAddressString(dto.getDeliveryAddress()))
                    .items(mapItems(dto.getItems()))
                    .build();

            logger.debug("Successfully converted OrderDto to Order entity: {}", order);
            return order;
        } catch (Exception e) {
            throw new RuntimeException("Error converting OrderDto to entity: %s".formatted(dto), e);
        }
    }

    public OrderDto toDto(Order entity) {
        logger.debug("Starting toDto conversion for Order entity: {}", entity);
        if (entity == null) {
            logger.warn("Order entity is null, returning null DTO");
            return null;
        }

        try {
            OrderDto orderDto = OrderDto.builder()
                    .orderId(entity.getOrderId().toString())
                    .customerId(entity.getCustomerId())
                    .status(entity.getStatus())
                    .shippingType(entity.getShippingType())
                    .timestamp(entity.getTimestamp())
                    .processedAt(entity.getProcessedAt())
                    .deliveryAddress(parseAddressString(entity.getDeliveryAddress()))
                    .items(mapToItemDtos(entity.getItems()))
                    .build();

            logger.debug("Successfully converted Order entity to OrderDto: {}", orderDto);
            return orderDto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting Order entity to DTO: %s".formatted(entity), e);
        }
    }

    private String extractAddressString(AddressDto addressDto) {
        logger.debug("Extracting address string from AddressDto: {}", addressDto);
        if (addressDto == null) {
            logger.warn("AddressDto is null, returning null address string");
            return null;
        }

        String addressString = String.format("%s, %s, %s",
                addressDto.getStreet(),
                addressDto.getCity(),
                addressDto.getZipCode());

        logger.debug("Extracted address string: {}", addressString);
        return addressString;
    }

    private List<OrderItem> mapItems(List<OrderItemDto> itemDtos) {
        logger.debug("Mapping {} OrderItemDto objects to OrderItem entities",
                itemDtos == null ? 0 : itemDtos.size());
        if (itemDtos == null) {
            logger.warn("ItemDtos list is null, returning empty list");
            return Collections.emptyList();
        }

        List<OrderItem> items = itemDtos.stream()
                .map(this::toItemEntity)
                .collect(Collectors.toList());

        logger.debug("Successfully mapped {} OrderItemDto objects to {} OrderItem entities",
                itemDtos.size(), items.size());
        return items;
    }

    private OrderItem toItemEntity(OrderItemDto dto) {
        logger.debug("Converting OrderItemDto to OrderItem entity: {}", dto);
        if (dto == null) {
            logger.warn("OrderItemDto is null, returning null entity");
            return null;
        }

        OrderItem orderItem = OrderItem.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .build();

        logger.debug("Successfully converted OrderItemDto to OrderItem: {}", orderItem);
        return orderItem;
    }

    private AddressDto parseAddressString(String address) {
        logger.debug("Parsing address string: {}", address);
        if (address == null) {
            logger.warn("Address string is null, returning null AddressDto");
            return null;
        }

        String[] parts = address.split(", ", 3);
        if (parts.length != 3) {
            logger.warn("Invalid address format. Expected 3 parts but got {} for address: {}", parts.length, address);
            return null;
        }

        AddressDto addressDto = AddressDto.builder()
                .street(parts[0])
                .city(parts[1])
                .zipCode(parts[2])
                .build();

        logger.debug("Successfully parsed address string to AddressDto: {}", addressDto);
        return addressDto;
    }

    private List<OrderItemDto> mapToItemDtos(List<OrderItem> items) {
        logger.debug("Mapping {} OrderItem entities to OrderItemDto objects",
                items == null ? 0 : items.size());
        if (items == null) {
            logger.warn("Items list is null, returning empty list");
            return Collections.emptyList();
        }

        List<OrderItemDto> itemDtos = items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());

        logger.debug("Successfully mapped {} OrderItem entities to {} OrderItemDto objects",
                items.size(), itemDtos.size());
        return itemDtos;
    }

    private OrderItemDto toItemDto(OrderItem entity) {
        logger.debug("Converting OrderItem entity to OrderItemDto: {}", entity);
        if (entity == null) {
            logger.warn("OrderItem entity is null, returning null DTO");
            return null;
        }

        OrderItemDto orderItemDto = OrderItemDto.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .build();

        logger.debug("Successfully converted OrderItem to OrderItemDto: {}", orderItemDto);
        return orderItemDto;
    }
}