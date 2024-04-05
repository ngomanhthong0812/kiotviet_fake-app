package com.example.kiotviet_fake.models;

public class Product {
    public int id;
    public String name;
    public String price;
    public int quantity;
    public int quantityOrder;

    public Product(int id, String name, String price, int quantity, int quantityOrder) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.quantityOrder = quantityOrder;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(int quantityOrder) {
        this.quantityOrder = quantityOrder;
    }
}
