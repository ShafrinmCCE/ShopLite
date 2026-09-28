package com.example.ShopLite.repository;

import com.example.ShopLite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.stockQuantity < p.reorderThreshold")
    List<Product> findBelowReorderLevel();

    List<Product> findByNameContainingIgnoreCase(String name);
}