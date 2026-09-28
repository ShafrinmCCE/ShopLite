package com.example.ShopLite.controller;

import com.example.ShopLite.model.Product;
import com.example.ShopLite.repository.ProductRepository;
import com.example.ShopLite.service.BillingService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillingService billingService;

    // Add product
    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }

    // Update product
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        product.setId(id);
        return productRepository.save(product);
    }

    // View low-stock products
    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts() {
        return billingService.getLowStock();
    }
}