package com.example.securityapplication.response;

import com.example.securityapplication.models.Order;

import java.util.List;

public class SearchResponse {
    private String message;
    private List<Order> orders;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
