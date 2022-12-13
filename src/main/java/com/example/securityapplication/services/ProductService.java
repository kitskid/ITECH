package com.example.securityapplication.services;

import com.example.securityapplication.models.PersonReact;
import com.example.securityapplication.models.Product;
import com.example.securityapplication.repositories.ProductRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findByName(Product product) throws UsernameNotFoundException {
        Optional<Product> prod = productRepository.findByName(product.getName());
        return prod.orElse(null);
    }
    public Product findById(Integer id){
        Optional<Product> product = productRepository.getProductById(id);
        return product.orElse(null);
    }
    @Transactional
    public void save(Product product){
        productRepository.save(product);
    }
    @Transactional
    public void deleteById(Integer id){
        productRepository.deleteById(id);
    }

    public Product getProductId(int id) {
        Optional<Product> product = productRepository.getProductById(id);
        return product.orElse(null);
    }
}
