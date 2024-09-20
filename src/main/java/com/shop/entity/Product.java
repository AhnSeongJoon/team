package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id;

    private String name;
    private String imgUrl;
    private String price;
    private String category;

    // 기본 생성자 추가
    public Product() {
    }

    public Product( String name, String imgUrl, String price, String category) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.price = price;
        this.category = category;
    }
}

