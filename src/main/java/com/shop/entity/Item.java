package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends  BaseEntity {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드

    @Column(nullable = false,length = 50)
    private String itemNm; // 상품명

    @Column(name = "price" , nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품판매 상태

    private String category;


    // private LocalDateTime regTime; //등록 시간

    // private LocalDateTime updateTime; //수정 시간

    @ManyToMany  (fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_item",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Member> member;

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.category = itemFormDto.getCategory();
    }
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber; // 10, 5/10,20
        if (restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량 : "+this.stockNumber+")");
        }
        this.stockNumber = restStock; // 5
    }
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

}
