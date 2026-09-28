package com.example.ShopLite.controller;

import com.example.ShopLite.dto.BillItemRequest;
import com.example.ShopLite.model.Bill;
import com.example.ShopLite.service.BillingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillingService billingService;

    // Create a bill
    @PostMapping
    public Bill createBill(@RequestBody List<BillItemRequest> requests) {
        return billingService.createBill(requests);
    }

    // Get total sales for a day
    @GetMapping("/sales")
    public double getDailySales(@RequestParam String date) {
        return billingService.getTotalSalesForDay(
                LocalDate.parse(date)
        );
    }
}