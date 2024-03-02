package com.example.orderservice.service;

import com.example.orderservice.dto.OrderInLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderInLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public void placedOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderInLineItemsList(orderRequest.getOrderInLineItemsDtoList().stream()
                .map(this::mapToDtoRequest)
                .collect(Collectors.toList()));
        orderRepository.save(order);
        log.info("Order was saved -> {}", order.getOrderNumber());
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    private OrderInLineItems mapToDtoRequest(OrderInLineItemsDto orderInLineItemsDto){
        OrderInLineItems order = new OrderInLineItems();
        order.setPrice(orderInLineItemsDto.getPrice());
        order.setQuantity(orderInLineItemsDto.getQuantity());
        order.setSkuCode(orderInLineItemsDto.getSkuCode());
        return order;
    }
}
