package com.example.securityapplication.controllers;

import com.example.securityapplication.enumm.Status;
import com.example.securityapplication.models.Cart;
import com.example.securityapplication.models.Order;
import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.models.Product;
import com.example.securityapplication.repositories.CartRepository;
import com.example.securityapplication.repositories.OrderRepository;
import com.example.securityapplication.response.AddProductResponse;
import com.example.securityapplication.security.PersonReactDetails;
import com.example.securityapplication.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE})
public class CartController {

    private final ProductService productService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CartController(ProductService productService, CartRepository cartRepository, OrderRepository orderRepository) {
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/cart/add/{id}")
    public ResponseEntity<?> addProductInCart(@PathVariable("id") int id){

        Product product = productService.getProductId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonReactDetails personDetails = (PersonReactDetails) authentication.getPrincipal();

        AddProductResponse response = new AddProductResponse();

        int id_person = personDetails.getPerson().getId();
        Cart cart = new Cart(id_person, product.getId());
        cartRepository.save(cart);

        response.setMessage("Товар добавлен в корзину");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    public List<Product> cart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonReactDetails personDetails = (PersonReactDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        List<Product> productsList = new ArrayList<>();
        for (Cart cart: cartList) {
            Product product = productService.getProductId(cart.getProductId());
            Product product1 = new Product();
            product1.setId(product.getId());
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
            product1.setProviderName(product.getProviderName());
            product1.setNameCategory(product.getNameCategory());
            product1.setFileName(product.getFileName());
            product1.setPrice(product.getPrice());
            productsList.add(product1);
        }
        return productsList;
    }

    @GetMapping("/cart/delete/{id}")
    public ResponseEntity<?> deleteProductCart(@PathVariable("id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonReactDetails personDetails = (PersonReactDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        cartList.removeIf(cart -> cart.getProductId() == id);

        AddProductResponse response = new AddProductResponse();
        response.setMessage("Товар добавлен в корзину");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/create")
    public ResponseEntity<?> order(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonReactDetails personDetails = (PersonReactDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        List<Product> productsList = new ArrayList<>();
        // Получаем продукты из корзины по id
        for (Cart cart: cartList) {
            productsList.add(productService.getProductId(cart.getProductId()));
        }

        float price = 0;
        for (Product product: productsList){
            price += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString();
        for (Product product: productsList){
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.ожидает_оплаты);
            orderRepository.save(newOrder);
            cartRepository.deleteCartByProductId(product.getId());
        }
        AddProductResponse response = new AddProductResponse();
        response.setMessage("Получен");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public List<Order> ordersUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonReactDetails personDetails = (PersonReactDetails) authentication.getPrincipal();
        return orderRepository.findByPersonReact(personDetails.getPerson());
    }
    @GetMapping("/orders/sale")
    public ResponseEntity<?> sale(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonReactDetails personDetails = (PersonReactDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findByPersonReact(personDetails.getPerson());
        for(Order order: orderList){

            if(order.getStatus() == Status.ожидает_оплаты){
                System.out.println(order.getStatus());
                order.setStatus(Status.оплачен);
                orderRepository.save(order);
            }
        }
        return ResponseEntity.ok("Оплата прошла успешно");
    }
}
