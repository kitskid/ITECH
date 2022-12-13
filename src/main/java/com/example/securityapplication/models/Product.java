package com.example.securityapplication.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Название товара не может быть пустым")
    @Size(min = 3, max = 20, message = "Имя должно быть от 3 и до 20 символов")
    @Column(name = "name", columnDefinition = "text", unique = true)
    private String name;

    @NotEmpty(message = "Необходимо описать товар")
    @Size(min = 10, max = 200, message = "Описание должно быть от 10 и до 200 символов")
    @Column(name = "description", columnDefinition = "text")
    private String description;

    @NotNull(message = "Укажите цену товара")
    @Min(1)
    @Column(name = "price", nullable = false)
    private int price;

   // @NotEmpty(message = "Укажите наименование поставщика товара")
    @Column(name = "providerName")
    private String providerName;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Provider provider;

    @Column(name = "filename")
    private String fileName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> imageList = new ArrayList<>();

    private LocalDateTime dateTime;

    @ManyToOne(optional = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Category category;

    private String nameCategory;
    private int cat_id;
    private int prov_id;

    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<PersonReact> personReactList;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @OneToMany(mappedBy = "product")
    private List<Order> orderList;

    // Метод по добавлению фотографий в лист к текущему продукту
    public void addImageToProduct(Image image){
        image.setProduct(this);
        imageList.add(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<PersonReact> getPersonReactList() {
        return personReactList;
    }

    public void setPersonReactList(List<PersonReact> personReactList) {
        this.personReactList = personReactList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public int getProv_id() {
        return prov_id;
    }

    public void setProv_id(int prov_id) {
        this.prov_id = prov_id;
    }
}
