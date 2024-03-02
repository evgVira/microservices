package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="t_order_in_line_items")
public class OrderInLineItems {

    @Id
    @GeneratedValue
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
