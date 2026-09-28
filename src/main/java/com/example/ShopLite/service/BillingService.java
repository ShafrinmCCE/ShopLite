package com.example.ShopLite.service;

import com.example.ShopLite.dto.BillItemRequest;
import com.example.ShopLite.model.Bill;
import com.example.ShopLite.model.BillItem;
import com.example.ShopLite.model.Product;
import com.example.ShopLite.repository.BillRepository;
import com.example.ShopLite.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillRepository billRepository;

    // Get products whose stock is below reorder level
    public List<Product> getLowStock() {
        return productRepository.findBelowReorderLevel();
    }

    // Create a bill
    public Bill createBill(List<BillItemRequest> requests) {

        // Check all quantities before changing stock
        for (BillItemRequest request : requests) {

            Product product = productRepository
                    .findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (request.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than zero");
            }

            if (request.getQuantity() > product.getStockQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for product: " + product.getName()
                );
            }
        }

        Bill bill = new Bill();
        bill.setItems(new ArrayList<>());

        double total = 0;

        // Reduce stock and create bill items
        for (BillItemRequest request : requests) {

            Product product = productRepository
                    .findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int quantity = request.getQuantity();

            product.setStockQuantity(
                    product.getStockQuantity() - quantity
            );

            productRepository.save(product);

            BillItem item = new BillItem();

            item.setBill(bill);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPriceAtSale(product.getPrice());

            bill.getItems().add(item);

            total += product.getPrice() * quantity;
        }

        bill.setTotalAmount(total);

        return billRepository.save(bill);
    }

    // Get total sales for a particular day
    public double getTotalSalesForDay(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Bill> bills =
                billRepository.findByBillDateBetween(start, end);

        double total = 0;

        for (Bill bill : bills) {
            total += bill.getTotalAmount();
        }

        return total;
    }
}