package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends  BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 외래키
    private Item item;


    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // 외래키
    private Order order;

    private int orderPrice;
    private int count;
    private String itemName;
    //  private LocalDateTime regTime;
   //  private LocalDateTime updateTime;

    //item(상품) -> OrderItem(주문상품)
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setItemName(item.getItemNm());
        item.removeStock(count);
        return orderItem;
    }
    public  int getTotalPrice(){
        return orderPrice*count;
    }
    public void cancel(){
        this.getItem().addStock(count);
    }
}
