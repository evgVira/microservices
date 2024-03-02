package com.example.prodcutservice.service;

import com.example.prodcutservice.dto.ProductRequest;
import com.example.prodcutservice.dto.ProductResponse;
import com.example.prodcutservice.model.Product;
import com.example.prodcutservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        log.info("Product was saved -> {}, {}", product.getId(), product.getName());
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProduct(){
        log.info("Show all products");
        return mapToProduct(productRepository.findAll());
    }

    private List<ProductResponse> mapToProduct(List<Product> products){
        List<ProductResponse> productResponses = products.stream()
                .map(product -> {
                return ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .description(product.getDescription())
                            .price(product.getPrice())
                            .build();
                }).collect(Collectors.toList());
        return productResponses;
    }

}
