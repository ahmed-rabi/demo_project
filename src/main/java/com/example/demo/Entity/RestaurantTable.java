package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "tablee")
@Data

public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long tableId;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "qr_code_value")
    private String qrCodeValue;

    @Column(name = "qr_code_image_url")
    private String qrCodeImageUrl;
}
