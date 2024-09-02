package com.shop.entity;

import com.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private int price;
    private String itemName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String orderUid; // 주문 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(int price, String itemName, String orderUid, Member member, Payment payment) {
        this.price = price;
        this.itemName=itemName;
        this.orderUid = UUID.randomUUID().toString();
        this.member = member;
        this.payment = payment;
    }

    // private LocalDateTime regTime;

    //private LocalDateTime updateTime;

    //주문서 주문아이템 리스트에 주문 아이템 추가
    //주문 아이템에 주문서 추가
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    // 주문서 생성
    // 현재 로그인된 멤버 주문서에 추가
    // 주문아이템 리스트를 반복문을 통해서 주문서에 추가
    // 상태는 주문으로 세팅
    // 주문 시간은 현재시간으로 세팅
    // 주문서 리턴
    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        order.orderUid = UUID.randomUUID().toString();
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
            order.price += orderItem.getOrderPrice()* orderItem.getCount();
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setItemName(orderItemList.get(0).getItemName());
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    // 주문서에 있는 주문 아이템 리스트를 반복
    // 주문 아이템마다 총 가격을 totalPrice 에 추가
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
}
