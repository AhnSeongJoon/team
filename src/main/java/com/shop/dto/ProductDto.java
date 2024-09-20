package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;            // 상품 ID
    private String name;        // 상품 이름
    private String imgUrl;      // 이미지 URL
    private String price;
    private String category;

    // 기본 생성자
    public ProductDto() {
    }

    // 모든 필드를 포함하는 생성자
    public ProductDto(Long id, String name, String imgUrl, String price, String category) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.price = price;
        this.category = category;
    }
}
