package com.example.android_kd_1;

import org.jetbrains.annotations.NotNull;

public class Product {
    private final String name;
    private final double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() { return price; }

    @NotNull
    @Override
    public String toString() {
        return name + " - €" + String.format("%.2f", price);
    }
}