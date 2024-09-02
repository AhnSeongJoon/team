package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.SessionUser;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import com.shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final HttpSession httpSession;
    private final MemberService memberService;


    @PostMapping(value = "/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult,
                         Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        // 로그인 정보 -> Spring Security
        // principal.getName() (현재 로그인된 정보)
        String email; // a@naver.com
        if (httpSession.getAttribute("user") != null){

            email = ((SessionUser)httpSession.getAttribute("user")).getEmail();
        }
        else {

            email=principal.getName();
        }
        Long orderId;
        try {

            orderId = orderService.order(orderDto,email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<OrderHistDto> orderHistDtoList;
        if(httpSession.getAttribute("user") != null){
            System.out.println("aaaaa");

            String email = ((SessionUser)httpSession.getAttribute("user")).getEmail();
            System.out.println("이메일"+email);
            System.out.println(pageable);
            orderHistDtoList = orderService.getOrderList(email,pageable);
        }
        else {
            System.out.println("bbbbb");
            orderHistDtoList = orderService.getOrderList(principal.getName(),pageable);
        }


        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);
        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){
        String email;
        if(httpSession.getAttribute("user") != null){
            System.out.println("aaaaa");
            email = ((SessionUser)httpSession.getAttribute("user")).getEmail();
        }
        else{
            System.out.println("bbbbb");
            email = principal.getName();
        }
        if(!orderService.validateOrder(orderId,email)){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
