package com.example.securityapplication.repositories;

import com.example.securityapplication.models.Order;
import com.example.securityapplication.models.PersonReact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPersonReact(PersonReact personReact);
}
