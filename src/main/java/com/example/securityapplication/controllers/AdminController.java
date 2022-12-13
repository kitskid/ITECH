package com.example.securityapplication.controllers;

import com.example.securityapplication.enumm.Status;
import com.example.securityapplication.models.Order;
import com.example.securityapplication.repositories.OrderRepository;
import com.example.securityapplication.response.AddUserResponse;
import com.example.securityapplication.response.SearchResponse;
import com.example.securityapplication.security.PersonReactDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE})
public class AdminController {

    private final OrderRepository orderRepository;

    public AdminController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/orders")
    public List<Order> ordersUser(){
        return orderRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/status")
    public Status[] allStatus(){
        return Status.values();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/update/status")
    public ResponseEntity<?> updateStatus(@RequestParam("order_id") String order_id, @RequestParam("status") String status){
        Order order = orderRepository.findById(Integer.parseInt(order_id)).orElse(null);
        AddUserResponse response = new AddUserResponse();
        if(order != null) {
            order.setStatus(Status.valueOf(status));
            orderRepository.save(order);

            response.setMessage("успешно изменили статус");
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("заказ не существует");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/search")
    public ResponseEntity<?> productSearch(@RequestParam("search_orders") String search){
        SearchResponse response = new SearchResponse();
        if(search == null){
            response.setMessage("поле не может быть пустым");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        } else if(!(search.length() == 4)){
            response.setMessage("в поле должно быть четыре символа");
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<Order> resultOrderList = new ArrayList<>();
            char[] chars = search.toCharArray();
            for(char s: chars){
                System.out.println(s);
            }
            List<Order> orderList = orderRepository.findAll();
            for(Order order: orderList){
                int k=0;
                System.out.println(order.getNumber());
                char[] charsOrder = order.getNumber().toCharArray();
                for(int i = 0; i < 4 ; i++){
                    System.out.println(chars[i]);
                    System.out.println(charsOrder[charsOrder.length - 4 + i]);
                    if(chars[i] == charsOrder[charsOrder.length - 4 + i]){
                        k++;
                        System.out.println("Нашли равные символы: " + chars[i]);
                    }
                }
                if(k == 4){
                    System.out.println("Нашли одного");
                    resultOrderList.add(order);
                }
            }

            response.setMessage("провели поиск");
            response.setOrders(resultOrderList);

        }
        return ResponseEntity.ok(response);
    }
}
