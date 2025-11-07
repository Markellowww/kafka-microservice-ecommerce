package com.markellowww.kafgen.controllers;

import com.markellowww.kafgen.OrderGenerator;
import com.markellowww.kafgen.models.Order;
import com.markellowww.kafgen.services.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Markelloww
 */

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderGenerator generator;

    public OrderController(OrderService orderService, OrderGenerator generator) {
        this.orderService = orderService;
        this.generator = generator;
    }

    @PostMapping
    public void createOrder() {
        Order order = generator.generateOrder();
        orderService.saveOrder(order);
    }
}
