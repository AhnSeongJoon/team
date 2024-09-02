package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "cart")
@ToString
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    // OneToOne 에서 one = cart / to / one = member 이다. 카트안에 멤버가 있다는 뜻
    @OneToOne (fetch = FetchType.LAZY) // 1:1 맵핑
    @JoinColumn(name = "member_id") // JoinColumn 매핑 할 외래키를 지정한다. 외래키 이름을 설정
    // name 을 명시하지 않으면 JPA 가 알아서 ID 를 찾지만 원하는 이름이 아닐 수도 있다.

    private Member member;

    public static Cart createCart (Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}


