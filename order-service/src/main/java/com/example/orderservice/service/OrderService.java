package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderInLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderInLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placedOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderInLineItems> orderInLineItems = orderRequest.getOrderInLineItemsDtoList()
                .stream()
                .map(orderInLineItemsDto -> mapToDtoRequest(orderInLineItemsDto)).toList();
        order.setOrderInLineItemsList(orderInLineItems);

        List<String> skuCodes = order.getOrderInLineItemsList().stream()
                .map(orderInLineItems1 -> orderInLineItems1.getSkuCode()).toList();

        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                .retrieve()
                                        .bodyToMono(InventoryResponse[].class)
                                                .block();
        boolean productInStock = Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());
        if(productInStock){
            orderRepository.save(order);
            log.info("Order was saved -> {}", order.getOrderNumber());
        }else{
            throw new IllegalArgumentException("Product is not in stock, try again later!");
        }
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
