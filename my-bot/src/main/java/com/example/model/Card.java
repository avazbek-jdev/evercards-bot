package com.example.model;

public class Card {
    private String name;
    private double price; 
    private String description;
    private String imageUrl;

    // Конструктор должен принимать 4 параметра!
    public Card(String name, double price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Эти геттеры ОБЯЗАТЕЛЬНО должны быть, чтобы DatabaseManager их видел
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}
