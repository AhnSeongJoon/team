package com.shop.pservice;

import com.shop.entity.Member;
import com.shop.entity.Order;

public interface OrderService {
    Order autoOrder(Member Member);
}

