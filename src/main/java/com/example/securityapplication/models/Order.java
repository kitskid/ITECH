package com.example.securityapplication.models;


import com.example.securityapplication.enumm.Status;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String number;
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne(optional = false)
    private Product product;
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne(optional = false)
    private PersonReact personReact;

    private int count;
    private float price;

    private LocalDateTime dateTime;

    private Status status;

    // Будем заполнять дату и время при создании объекта класса
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PersonReact getPersonReact() {
        return personReact;
    }

    public void setPerson(PersonReact personReact) {
        this.personReact = personReact;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    @Transactional
    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonReact(PersonReact personReact) {
        this.personReact = personReact;
    }

    public Order() {
    }

    public Order(String number, Product product, PersonReact personReact, int count, float price, Status status) {
        this.number = number;
        this.product = product;
        this.personReact = personReact;
        this.count = count;
        this.price = price;
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
