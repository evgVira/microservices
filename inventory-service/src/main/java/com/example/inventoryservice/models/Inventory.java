package com.example.inventoryservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="t_inventory")
public class Inventory {
    @Id
    @GeneratedValue
    private Long id;
    private String skuCode;
    private Integer quantity;

}
