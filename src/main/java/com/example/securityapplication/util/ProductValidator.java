package com.example.securityapplication.util;


import com.example.securityapplication.models.Product;
import com.example.securityapplication.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {

    private final ProductService productService;

    public ProductValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

        if(productService.findByName(product) != null){
            errors.rejectValue("name", "", "Данное имя товара уже занято");
        }

    }
}
