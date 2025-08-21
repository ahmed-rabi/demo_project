package com.example.demo.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "price")
    private int price;

    // Foreign key to Category
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(
//            name = "category_id",
//            nullable = false,
//            foreignKey = @ForeignKey(name = "fk_category")
//    )
//    private Category category;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

//    public Category getCategory() { return category; }
//    public void setCategory(Category category) { this.category = category; }
}
