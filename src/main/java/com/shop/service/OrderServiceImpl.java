package com.shop.service;

import com.shop.order.POrderRepository;
import com.shop.constant.PaymentStatus;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.Payment;
import com.shop.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.shop.pservice.OrderService;

import java.util.UUID;



@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final POrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Order autoOrder(Member member) {

        // 임시 결제내역 생성
        Payment payment = Payment.builder()
                .price(1000)
                .status(PaymentStatus.READY)
                .build();

        paymentRepository.save(payment);

        // 주문 생성
        Order order = Order.builder()
                .price(1000)
                .itemName("1달러샵 상품")
                .orderUid(UUID.randomUUID().toString())
                .member(member)
                .payment(payment)
                .build();

        return orderRepository.save(order);
    }
}