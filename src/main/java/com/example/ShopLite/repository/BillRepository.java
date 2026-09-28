package com.example.ShopLite.repository;

import com.example.ShopLite.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByBillDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}