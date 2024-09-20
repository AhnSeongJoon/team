package com.shop.controller;

import com.shop.dto.ProductDto;
import com.shop.entity.Product;
import com.shop.repository.ProductRepository;
import com.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String getProducts(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Product> products;

        // 카테고리 값이 "전체"거나 null인 경우, 모든 상품을 가져옴
        if (category == null || category.isEmpty() || category.equals("전체")) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByCategory(category);
        }

        model.addAttribute("products", products);
        return "product-list"; // Thymeleaf 템플릿 파일명
    }
    @GetMapping("/product/detail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.getCrawledProductById(id);
        model.addAttribute("product", product);
        return "productDtl";  // productDtl.html로 연결
    }

    @GetMapping("/product-list")
    public String getProducts(Model model) {
        try {
            // Selenium을 사용해 크롤링한 데이터 가져오기
            List<Product> products = productService.getProductDatas(); // getProductDatas 메서드 호출
            model.addAttribute("products", products); // 뷰로 크롤링한 데이터 전달
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 로그 출력
            model.addAttribute("error", "상품 데이터를 불러오는 중 문제가 발생했습니다."); // 에러 메시지를 뷰에 전달
        }
        return "product-list"; // 반환할 뷰의 파일명 (product-list.html)
    }


}
