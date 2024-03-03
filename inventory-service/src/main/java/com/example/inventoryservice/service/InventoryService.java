package com.example.inventoryservice.service;

import com.example.inventoryservice.models.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        Optional<Inventory> inventory = inventoryRepository.findBySkuCode(skuCode);
        if(inventory.isPresent()){
            return inventory.get().getQuantity() > 0;
        }else {
            return false;
        }
    }
}
